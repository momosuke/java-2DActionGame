import java.awt.*;
import java.awt.image.*;

/**
 * 敵の構成
 * @author momosuke
 * @version 1.0
 */
public class Enemy extends Sprite {
	final double gravity=0.1;
	private double eex, eey;
	private double vx=-1, vy=0;
	private boolean left=true, right=false;
	private int l, r, u, d;
	
	/**
	 * 構成要素
	 * @param G 位置合わせ
	 */
	public Enemy(
		double x, double y, int[] G, Image Img, Map map,
		int ll, int rr, int uu, int dd
		){
		super(x, y, G, Img, map);
		e1Img = Img;
		eex=x; eey=y;
		l=ll; r=rr; u=uu; d=dd;
	}
	
	/**
	 * 各状態に対する障害物との当たり判定および速度、位置補正
	 */
	public void update(){
		
		int lexvx=(int)((x- l+vx)/Adv.pixel);
		int  exvx=(int)((x   +vx)/Adv.pixel);
		int rexvx=(int)((x+ r+vx)/Adv.pixel);
		int ueyvy=(int)((y- u+vy)/Adv.pixel);
		int  eyvy=(int)((y   +vy)/Adv.pixel);
		int deyvy=(int)((y+ d+vy)/Adv.pixel);
		
		int lexx=(int)((x- l    )/Adv.pixel);
		int  exx=(int)((x       )/Adv.pixel);
		int rexx=(int)((x+ r    )/Adv.pixel);
		int ueyy=(int)((y- u    )/Adv.pixel);
		int  eyy=(int)((y       )/Adv.pixel);
		int deyy=(int)((y+ d    )/Adv.pixel);
		
		if( lexvx<=map.MX+1 && lexvx>=0 && rexvx<=map.MX+1 && rexvx>=0 &&
		    ueyvy<=map.MY+1 && ueyvy>=0 && deyvy<=map.MY+1 && deyvy>=0 &&
		     exvx<=map.MX+1 &&  exvx>=0 &&  eyvy<=map.MY+1 &&  eyvy>=0 &&
		     lexx<=map.MX+1 &&  lexx>=0 &&  rexx<=map.MX+1 &&  rexx>=0 &&
		     ueyy<=map.MY+1 &&  ueyy>=0 &&  deyy<=map.MY+1 &&  deyy>=0 &&
		      exx<=map.MX+1 &&   exx>=0 &&   eyy<=map.MY+1 &&   eyy>=0 ){
		
			if(up){
				if( map.data[ueyvy][lexx]=='B' ||
						map.data[ueyvy][ exx]=='B' ||
						map.data[ueyvy][rexx]=='B'   ) {
							//vy=-vy;
							vy=0;
							y=(ueyvy+2)*Adv.pixel-2;
						}
			}
			if(left){
				if( map.data[ueyy][lexvx]=='B' ||
						map.data[ eyy][lexvx]=='B' ||
						map.data[deyy][lexvx]=='B'   ) {
							vx=-vx;
							right=true;
							left =false;
							int[] lg={16,24,16,0,0,24,-8,-13};
							G=lg;
						}
			}
			if(right){
				if( map.data[ueyy][rexvx]=='B' ||
						map.data[ eyy][rexvx]=='B' ||
						map.data[deyy][rexvx]=='B'   ) {
							vx=-vx;
							left=true;
							right=false;
							int[] rg={16,24,0,0,16,24,-8,-13};
							G=rg;
						}
			}
			if(onGraund){
				if(	map.data[deyvy][lexx]!='B' &&
						map.data[deyvy][ exx]!='B' &&
						map.data[deyvy][rexx]!='B'   ) {
							onGraund=false;
						}
			}else if(!onGraund){
				//vy+=gravity;
				if(	map.data[deyvy][lexx]=='B' ||
						map.data[deyvy][ exx]=='B' ||
						map.data[deyvy][rexx]=='B'   ) {
							//vy=-vy;
							y=(deyvy)*Adv.pixel-11;
							onGraund=true;
						}
			}
			
		}
		
		if(vy>5) vy=5;
		eex+=vx;
		x=(int)(eex);
		y+=(int)(vy);
		
	}
	
}
