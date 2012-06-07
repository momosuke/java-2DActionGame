import java.awt.*;
import java.awt.image.*;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * マップの構成
 * @author momosuke
 * @version 1.0
 */
public class Map{
	ImageIcon icon;
	Image b1Img;
	Image b2Img;
	Image e1Img;
	Image rcImg;
	Image c1Img;
	Image c2Img;
	Image m1Img;
	char data[][];
	private LinkedList sprites;
	Chara chara;
	int MX=0;
	int MY=0;
	
	/**
	 * マップの生成
	 * @param url マップデータのアドレス
	 */
	public Map(String url){
	
		try {
			BufferedReader ar = new BufferedReader(new InputStreamReader(
			getClass().getResourceAsStream(url)));
			String read = "start";
			for(MY=0; read!=null; MY++){
				read = ar.readLine();
				if(MY==0) MX = read.length()/5;
			}
			
		} catch (Exception e) {e.printStackTrace();}
		MX = MX - 1;
		MY = MY - 2;
		data = new char[MY+2][MX+2];
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
			getClass().getResourceAsStream(url)));
			for (int y = 0; y <= MY; y++) {
				String line = br.readLine();
				for (int x = 0; x <= MX; x++) {
					data[y][x] = line.charAt(x*5+3);
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		
		loadImage();
		
		// 敵の生成
		sprites=new LinkedList();
		int[] eG={16,24,0,0,16,24,-8,-13};	//{width,height,stx,sty,dx,dy}
		for(int y=1;y<=MY;y++){
			for(int x=1;x<=MX;x++){
				switch(data[y][x]){
					case 'E': 
					sprites.add(new Enemy((double)(Adv.pixel*x), 
					(double)(Adv.pixel*y), eG, e1Img, this, 5, 7, 10, 10));
					break;
				}
			}
		}
	}
	
	/**
	 * イメージの読み込み
	 */
	private void loadImage() {
		icon = new ImageIcon(getClass().getResource("image/block1.gif"));
						b1Img = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/block2.gif"));
						b2Img = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/rockman.gif"));
						rcImg = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/chara1.gif"));
						c1Img = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/chara2.gif"));
						c2Img = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/enemy1.gif"));
						e1Img = icon.getImage();
		icon = new ImageIcon(getClass().getResource("image/stg_turbo.gif"));
						m1Img = icon.getImage();
	}
	
	/**
	 * マップの描画
	 */
	public void draw(Graphics g, int setX, int setY, String url, ImageObserver observer){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
			getClass().getResourceAsStream(url)));
			for (int y = 0; y <= MY; y++) {
				String line = br.readLine();
				for (int x = 0; x <= MX; x++) {
					String d2 = line.substring(x*5,x*5+1);
					int data2 = Integer.parseInt(d2,16);
					String d3 = line.substring(x*5+1,x*5+2);
					int data3 = Integer.parseInt(d3,16);
					g.drawImage(m1Img, 
						Adv.pixel*x+setX,     Adv.pixel*y+setY,
						Adv.pixel*x+16+setX,  Adv.pixel*y+16+setY,
						data3*16,         data2*16,
						data3*16+16,      data2*16+16, observer);
				}
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * スプライトの衝突判定
	 */
	public boolean Collision(Sprite sprite){
		int sx = (int)(sprite.getX()/Adv.pixel);
		int sy = (int)(sprite.getY()/Adv.pixel);
		if(sx<=MX && sy<=MY){
			if(data[sy][sx]=='B') return true;
		}
		return false;
	}
	
	/**
	 * スプライトの読み込み
	 */
	public LinkedList getSprites(){
		return sprites;
	}
	
	/**
	 * プレイヤーの生成
	 */
	public Chara getCharactor(){
		int[] G={10,20,0,0,16,24,-10,-13};
		int clf=30;
		for (int y=1,i=0; y<=MY; y++){
			for (int x=1; x<=MX; x++){
				switch( data[y][x] ){
					case 'C': 
					chara = new Chara(Adv.pixel*x, Adv.pixel*y, G, 
						rcImg, this, c1Img, c2Img, clf);
					break;
				}
			}
		}
		return chara;
	}
	
}
