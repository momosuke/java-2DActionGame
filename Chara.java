import java.awt.*;
import java.awt.image.*;
import java.applet.Applet;

/**
 * キャラクタの構成
 * @author momosuke
 * @version 1.0
 */
public class Chara extends Sprite {
	final int[][] position = {
		/*0*/	{ 15, 13, 26-15, 28-13, 303, 4, 303+26, 4+28 },   
		/*1*/	{ 11, 13, 26-15, 28-13, 303+26, 4, 303, 4+28 },   
		/*2*/	{ 12, 13, 29-12, 22-13, 40, 45, 40+29, 45+22 },   
		/*3*/	{ 18, 20, 32-18, 32-20, 0, 0, 32, 32 },           
		/*4*/	{ 16, 13, 29-16, 22-13, 40+29, 45, 40, 45+22 },   
		/*5*/	{ 13, 20, 32-13, 32-20, 32, 0, 0, 32 },           
		/*6*/	{ 11, 14, 31-11, 24-14, 0, 45, 0+31, 45+24 },     
		/*7*/	{ 15, 21, 32-15, 32-21, 0, 0, 32, 32 },           
		/*8*/	{ 20, 14, 31-20, 24-14, 0+31, 45, 0, 45+24 },     
		/*9*/	{ 16, 21, 32-16, 32-21, 32, 0, 0, 32 },           
		/*10*/	{ 15, 13, 29-15, 30-13, 269, 4, 269+29, 4+30 },   
		/*11*/	{ 15, 13, 26-15, 30-13, 237, 2, 237+26, 2+30 },   
		/*12*/	{ 14, 13, 29-14, 30-13, 269+29, 4, 269, 4+30 },   
		/*13*/	{ 11, 13, 26-11, 30-13, 237+26, 2, 237, 2+30 },   
	};
	final int[]  bG={8,6,348,4,348+7,4+5,-4,-3};
	final double gravity=0.6;
	double ccx, ccy, vx, vy, svx, bx, by, time=0.0;
	boolean left=false, right=false, hit=false;
	Bullet bullet;
	
	/**
	 * 構成要素
	 * @param G 位置合わせ
	 * @param lf ライフポイント
	 */
	public Chara(double x, double y, int[] G, Image Img, Map map, Image c1, Image c2, int lf){
		super(x, y, G, Img, map);
		rcImg=Img; c1Img=c1; c2Img=c2;
		life=lf;
		ccx=x; ccy=y;
	}
	
	/**
	 * 移動量の計算
	 * @param the 行動状態（右、左、ジャンプ、ショット）
	 * @param judg 行動状態の有無を判断
	 * @param dt 時間／flame
	 */
	public void move(int the, boolean judg, double dt){
		time+=dt;
		int t = ((int)(time*100)%100);
		if(judg){
			switch(the){
				case 0: if(right) vx=0;
					else      vx=-3.2;
					left=true;
					break;
				case 1: if(left) vx=0;
					else     vx=3.2;
					right=true;
					break;
				case 2: if(onGraund){
						vy=-10;
						onGraund=false;
					}
					break;
				case 3: if(onGraund){
						if(leftdirect){
							bx=x-23+4; by=y-3; svx=-4;
						}else if(rightdirect){
							bx=x+21-4; by=y-3; svx= 4;
						}
					}else if(!onGraund){
						if(leftdirect){
							bx=x-23+8; by=y-3; svx=-4;
						}else if(rightdirect){
							bx=x+21-8; by=y-3; svx= 4;
						}
					}
					bullet = new Bullet(bx, by, bG, rcImg, map, svx);
					shot=true; time=0;
					break;
			}
		}else{
			switch(the){
				case 0: if(!right) vx=0;  left=false; break;
				case 1: if(!left)  vx=0; right=false; break;
				case 3: if(t>20) {shot=false; time=0;} break;
			}
		}
	}
	
	/**
	 * 各状態に対する障害物との当たり判定および速度、位置補正
	 */
	public void update(){
		
		int lcxvx=(int)((x- 5+vx)/Adv.pixel);
		int  cxvx=(int)((x   +vx)/Adv.pixel);
		int rcxvx=(int)((x+ 5+vx)/Adv.pixel);
		int ucyvy=(int)((y-10+vy)/Adv.pixel);
		int  cyvy=(int)((y   +vy)/Adv.pixel);
		int dcyvy=(int)((y+10+vy)/Adv.pixel);
		
		int lcxx=(int)((x- 5    )/Adv.pixel);
		int  cxx=(int)((x       )/Adv.pixel);
		int rcxx=(int)((x+ 5    )/Adv.pixel);
		int ucyy=(int)((y-10  -1)/Adv.pixel);
		int  cyy=(int)((y     -1)/Adv.pixel);
		int dcyy=(int)((y+10  -1)/Adv.pixel);
		
		if( lcxvx<=map.MX+1 && lcxvx>=0 && rcxvx<=map.MX+1 && rcxvx>=0 &&
		    ucyvy<=map.MY+1 && ucyvy>=0 && dcyvy<=map.MY+1 && dcyvy>=0 &&
		     cxvx<=map.MX+1 &&  cxvx>=0 &&  cyvy<=map.MY+1 &&  cyvy>=0 &&
		     lcxx<=map.MX+1 &&  lcxx>=0 &&  rcxx<=map.MX+1 &&  rcxx>=0 &&
		     ucyy<=map.MY+1 &&  ucyy>=0 &&  dcyy<=map.MY+1 &&  dcyy>=0 &&
		      cxx<=map.MX+1 &&   cxx>=0 &&   cyy<=map.MY+1 &&   cyy>=0 ){
			
			if(up){
				if( map.data[ucyvy][lcxx]=='B' ||
						map.data[ucyvy][ cxx]=='B' ||
						map.data[ucyvy][rcxx]=='B'   ) {
							vy=0;
							y=(ucyvy+2)*Adv.pixel-2;
						}
			}
			if(left){
				if( map.data[ucyy][lcxvx]=='B' ||
						map.data[ cyy][lcxvx]=='B' ||
						map.data[dcyy][lcxvx]=='B'   ) {
							vx=0;
						}
			}
			if(right){
				if( map.data[ucyy][rcxvx]=='B' ||
						map.data[ cyy][rcxvx]=='B' ||
						map.data[dcyy][rcxvx]=='B'   ) {
							vx=0;
						}
			}
			if(onGraund){
				if(	map.data[dcyvy][lcxx]!='B' &&
						map.data[dcyvy][ cxx]!='B' &&
						map.data[dcyvy][rcxx]!='B'   ) {
							onGraund=false;
						}
			}else if(!onGraund){
				vy+=gravity;
				if(	map.data[dcyvy][lcxx]=='B' ||
						map.data[dcyvy][ cxx]=='B' ||
						map.data[dcyvy][rcxx]=='B'   ) {
							vy=0;
							y=(dcyvy)*Adv.pixel-10;
							onGraund=true;
							Adv.playClip(Sound.TYAKUTI);
						}
			}
			
		}
		
		if(vy>10) vy=10;
		ccx+=vx;
		x=(int)(ccx);
		y+=(int)(vy);
		
	}
	
	/**
	 * 敵とのヒット状態の有無を更新
	 */
	public void hit(boolean collision){
		hit=collision;
	}
	
	/**
	 * プレイヤーの描画処理
	 */
	public void draw(Graphics g, int setX, int setY, ImageObserver observer){
		
		int[] pos = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int cx=(int)x;
		int cy=(int)y;
		
		if(right){
			rightdirect=true;
			leftdirect=false;
		}else if(left){
			rightdirect=false;
			leftdirect=true;
		}
		
		Img = rcImg;
		
		if(hit){
			if(rightdirect)     pos = position[0];
			else if(leftdirect) pos = position[1];
		}else{
			if(onGraund){
				if(right){
					if(shot) pos = position[2];
					else    {pos = position[3]; Img = c2Img;}
				}else if(left){
					if(shot) pos = position[4];
					else    {pos = position[5]; Img = c2Img;}
				}else{
					if(rightdirect){
						if(shot) pos = position[6];
						else    {pos = position[7]; Img = c1Img;}
					}else if(leftdirect){
						if(shot) pos = position[8];
						else    {pos = position[9]; Img = c1Img;}
					}
				}
			}else if(!onGraund){
				if(rightdirect){
					if(shot) pos = position[10];
					else     pos = position[11];
				}else if(leftdirect){
					if(shot) pos = position[12];
					else     pos = position[13];
				}
			}
		}
		
		g.drawImage(
			Img, 
			cx+setX-pos[0],  cy+setY-pos[1],
			cx+setX+pos[2],  cy+setY+pos[3],
			pos[4],          pos[5],
			pos[6],          pos[7], observer
		);
		
	}
	
}
