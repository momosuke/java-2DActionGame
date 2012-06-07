import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.*;
import java.io.File;

/**
 * ゲーム本体
 * @author momosuke
 * @version 1.0
 */
public class Adv extends JPanel implements Runnable {
	
	// パネルサイズ
	public static final int current_width = 400;
	public static final int current_height = 280;
	
	// time
	private double time;
	private double PlayTime;
	private double flame;
	private double dt = 0.0;
	
	// judge
	private boolean flag  = true;
	private boolean GameStart = true;
	private boolean shot = false;
	private boolean goal = false;
	private boolean collision = false;
	private boolean newgame = true;
	private boolean pose = false;
	
	// applet
	public static final int pixel = 16;
	private int setX;
	private int setY;
	private int Z = 0;
	
	// offscreen
	private Image offscreenImg;
	private Graphics offscreenG;
	
	// map
	private Map[] maps;
	private int STAGE = 0;
	private static final int STAGE_NUM = 1;
	
	// game
	private int gameState = GameState.TITLE;
	
	// chara
	private Chara chara;
	private int pcx;
	private int pcy;
	
	// sprites
	private LinkedList enemies;
	private LinkedList bullets;
	
	// sound
	private static final String[] bgmNames = {"flashxg.mid", "flashxg.mid"};
	private static final String[] seNames = {"shot.wav", "hit.wav", "damage.wav", "deth.wav", "tyakuti.wav"};
	private static final String[] mapNames = {"map/map01.dat", "map/map02.dat"};
	
	// key
	private ActionKey leftKey = new ActionKey();
	private ActionKey rightKey = new ActionKey();
	private ActionKey jumpKey = new ActionKey(ActionKey.DETECT_PRESS);
	private ActionKey downKey = new ActionKey(ActionKey.DETECT_PRESS);
	private ActionKey shotKey = new ActionKey(ActionKey.DETECT_PRESS);
	private ActionKey spaceKey = new ActionKey(ActionKey.DETECT_PRESS);
	private ActionKey resetKey = new ActionKey(ActionKey.DETECT_PRESS);
	
	// ゲームループ用スレッド
	private Thread gameLoop;
	
	/**
	 * 初期設定
	 */
	public Adv(){
	
		// パネルの推奨サイズを設定、pack()するときに必要
		setPreferredSize(new Dimension(current_width, current_height));
		
		// パネルがキー入力を受け付けるようにする
		setFocusable(true);
			
		// sound Get
		loadSound();
		
		// キーボードイベント処理
		addKeyListener(
		new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				int the = -1;
			if(!collision){
				/*L*/ if(key==37){the = 0; leftKey.press();}
				/*R*/ if(key==39){the = 1; rightKey.press();}
				/*U*/ if(key==38){the = 2; jumpKey.press();}
				/*Z*/ if(key==90){the = 3; shotKey.press();}
				/*D*/ if(key==40){the = 4; downKey.press();}
			}
				/*S*/ if(key==32){spaceKey.press();}
				/*X*/ if(key==88){resetKey.press();}
			}
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				int the = -1;
				if(key==37){the = 0; leftKey.release();}
				if(key==39){the = 1; rightKey.release();}
				if(key==38){the = 2; jumpKey.release();}
				if(key==90){the = 3; shotKey.release();}
				if(key==40){the = 4; downKey.release();}
				if(key==32){spaceKey.release();}
				if(key==88){resetKey.release();}
			}
		}
		);
		
		// アプレットの背景
		setBackground(Color.black);
		
		GameStart();
	}
	
	/**
	 * ゲームの開始（MAP、SPRITEの生成）
	 */
	public void GameStart(){
	
		// MAPの生成
		maps = new Map[2];
		maps[0] = new Map(mapNames[0]);
		maps[1] = new Map(mapNames[1]);
		
		// CHARAの生成
		bullets = new LinkedList();
		enemies = maps[STAGE].getSprites();
		chara = maps[STAGE].getCharactor();
		
		// ゲームループ開始
		gameLoop = new Thread(this);
		gameLoop.start();
		
	}
	
	/**
	 * ゲーム動作中の処理
	 */
	public void run(){
		flag = true;
		PlayTime = 0.0;
		flame = 1;
		if(GameStart){
			GameStart = false;
			while(flag){
				switch(gameState){
					case GameState.TITLE:
						if(newgame && spaceKey.isPressed()){
							time = System.currentTimeMillis()*0.001;
							MidiEngine.play(STAGE);
							gameState = GameState.MAIN;
						}
						repaint();
						break;
					case GameState.MAIN:
						try{Thread.sleep(19);} catch(Exception ex){}
						moveTime();
						movePlayer();
						chara.update();
						Collision();
						if(resetKey.isPressed()) Pose();
						repaint();
						break;
					case GameState.NEXT:
						if(spaceKey.isPressed()){
							time = System.currentTimeMillis()*0.001;
							MidiEngine.play(STAGE);
							gameState = GameState.MAIN;
						}
						repaint();
						break;
					case GameState.CLEAR:
						if(spaceKey.isPressed()){
							gameState = GameState.TITLE;
						}
						repaint();
						break;
				}
			}
		}
		GameStart = true;
	}
	
	/**
	 * 当たり判定処理
	 */
	public void Collision(){
	
		//Player collision Enemie
		Iterator enerator=enemies.iterator();
		while(enerator.hasNext()){
			Sprite e1=(Sprite)enerator.next();
			e1.update();
			chara.hit(collision);
			if(flame>1){
				collision=false;
				if(chara.isCollision(e1)){
					collision=true;
					playClip(Sound.DAMAGE);
					chara.life-=10;
					leftKey.release();
					rightKey.release();
					flame=0;
					//chara.ccx-=10;
					chara.vy=0;
					if(chara.life<0) GameOver();
					break;
				}
			}
		}
		
		//Enemie collision Ballet
		if(shot){
			bullets.add(chara.bullet);
			playClip(Sound.SHOT);
		}
		Iterator gunrator=bullets.iterator();
		
		outer: while(gunrator.hasNext()){
			Sprite gun1=(Sprite)gunrator.next();
			gun1.update();
			enerator=enemies.iterator();
			if(maps[STAGE].Collision(gun1)){
				Bullet bullet=(Bullet)gun1;
				bullets.remove(bullet);
				break outer;
			}
			while(enerator.hasNext()){
				Sprite e2=(Sprite)enerator.next();
				if(gun1.isCollision(e2)){
					playClip(Sound.HIT);
					Enemy  enemy =(Enemy) e2;
					Bullet bullet=(Bullet)gun1;
					enemies.remove(enemy);
					bullets.remove(bullet);
					break outer;
				}
			}
		}
		
	}
	
	/**
	 * 次のステージ準備
	 */
	public void GameNext(){
		flag=false;
		collision=false;
		gameState = GameState.NEXT;
		STAGE++;
		MidiEngine.close();
		GameStart();
	}
	
	/**
	 * ゲーム終了
	 */
	public void GameOver(){
		flag=false;
		collision=false;
		playClip(Sound.DETH);
		gameState = GameState.TITLE;
		STAGE=0;
		MidiEngine.close();
		GameStart();
	}
	
	/**
	 * ゲームクリア
	 */
	public void GameClear(){
		flag=false;
		collision=false;
		gameState = GameState.CLEAR;
		STAGE=0;
		MidiEngine.close();
		GameStart();
	}
	
	/**
	 * ゲーム中断
	 */
	public void Pose(){
		MidiEngine.stop();
		pose = true;
		while(pose) {
			try{Thread.sleep(19);} catch(Exception ex){}
			if(resetKey.isPressed()){
				pose = false;
				MidiEngine.start(STAGE);
			}
		}
		time += System.currentTimeMillis()*0.001-time;
	}
	
	/**
	 * ゲームの描画
	 */
	public void paintComponent(Graphics g) {
	
		// 背景を黒で塗りつぶす
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		switch(gameState){
		
		case GameState.TITLE:
		
			g.setFont(new Font("ＭＳ ゴシック",Font.BOLD,25));
			g.setColor(Color.white);
			g.drawString("ROCK MAN",current_width/2-55,current_height/2-30);
			g.setFont(new Font("ＭＳ ゴシック",Font.BOLD,10));
			g.drawString("NEW GAME",current_width/2-25,current_height/2+30);
			g.drawString("CONTINUE",current_width/2-25,current_height/2+55);
			     if(downKey.isPressed()) {Z=25; newgame=false;}
			else if(jumpKey.isPressed()) {Z=0;  newgame=true;}
			int[] cursorX={current_width/2-40, current_width/2-40, current_width/2-30};
			int[] cursorY={current_height/2+21+Z, current_height/2+31+Z, current_height/2+26+Z};
			g.fillPolygon(cursorX, cursorY, 3);
			break;
			
		case GameState.MAIN:
		
			// マップの描画
			setX = (current_width)/2 - (int)chara.getX();
			setX = Math.min(setX, 0);
			setX = Math.max(setX, (current_width) - (maps[STAGE].MX+1)*pixel);
			setY = (current_height)/2 - (int)chara.getY();
			setY = Math.min(setY, 0);
			setY = Math.max(setY, (current_height) - (maps[STAGE].MY+1)*pixel);
			maps[STAGE].draw(g,setX,setY,mapNames[STAGE],this);
			
			// キャラの描画
			chara.draw(g,setX,setY,this);
			
			// 敵の描画
			Iterator enerator=enemies.iterator();
			while(enerator.hasNext()){
				Sprite emy=(Sprite)enerator.next();
				emy.draw(g,setX,setY,this);
			}
			
			// 弾の描画
			Iterator gunrator=bullets.iterator();
			while(gunrator.hasNext()){
				Sprite gun=(Sprite)gunrator.next();
				gun.draw(g,setX,setY,this);
			}
			
			// プレイ時間
			g.setColor(Color.white);
			g.setFont(new Font("ＭＳ ゴシック",Font.BOLD,14));
			int minute = ((int)(PlayTime/60)%60);
			int second =  (int)(PlayTime%60);
			int connma = ((int)(PlayTime*100)%100);
			if(minute<10){g.drawString("0"+minute+":",current_width-87,pixel*3);
				}else{g.drawString(    minute+":",current_width-87,pixel*3);}
			if(second<10){g.drawString("0"+second+":",current_width-62,pixel*3);
				}else{g.drawString(    second+":",current_width-62,pixel*3);}
			if(connma<10){g.drawString("0"+connma+" ",current_width-37,pixel*3);
				}else{g.drawString(    connma+" ",current_width-37,pixel*3);}
			
			// LIFE GAGE
			g.setColor(Color.yellow);
			for(int i=0;i<=chara.life;i+=2){
				g.fillRect(pixel*2,70-i,10,1);
			}
			break;
			
		case GameState.NEXT:
			g.setFont(new Font("ＭＳ ゴシック",Font.BOLD,17));
			g.setColor(Color.white);
			g.drawString("NEXT STAGE",current_width/2-50,current_height/2+5);
			break;
			
		case GameState.CLEAR:
			g.setFont(new Font("ＭＳ ゴシック",Font.BOLD,17));
			g.setColor(Color.white);
			g.drawString("GAME CLEAR",current_width/2-50,current_height/2+5);
			break;
			
		}
		
	}
	
	/**
	 * 動作時間
	 */
	public void moveTime(){
		dt = System.currentTimeMillis()*0.001-time;
		time     += dt;
		PlayTime += dt;
		flame    += dt;
	}
	
	/**
	 * プレイヤーの動作
	 */
	public void movePlayer(){
		pcx=(int)(chara.getX()/pixel);
		pcy=(int)(chara.getY()/pixel);
		if(pcx<=maps[STAGE].MX && pcy<=maps[STAGE].MY){
			if(maps[STAGE].data[pcy][pcx]=='G'){
				if(STAGE_NUM>STAGE) GameNext();
				else GameClear();
			}
		}
		if(flag){
			shot=shotKey.isPressed();
			chara.move(0,leftKey.isPressed(),0);
			chara.move(1,rightKey.isPressed(),0);
			chara.move(2,jumpKey.isPressed(),0);
			chara.move(3,shot,dt);
		}
	}
	
	/**
	 * 音楽の読み込み
	 */
	private void loadSound() {
		for (int i=0; i<bgmNames.length; i++) {
			try {
				MidiEngine.load("mid/" + bgmNames[i]);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 効果音の再生
	 */
	public static void playClip(int NUM) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream in = 
				AudioSystem.getAudioInputStream(new File("wav/"+seNames[NUM]));
			clip.open(in);
			FloatControl control = 
				(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			controlByLinearScalar(control, 2.0); // 200%の音量で再生する
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 音量調節
	 */
	private static void controlByLinearScalar(FloatControl control, double linearScalar) {
		control.setValue((float)Math.log10(linearScalar) * 20);
	}
	
}
