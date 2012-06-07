import java.awt.*;
import java.awt.image.*;
import java.awt.Rectangle;

/**
 * スプライトの構成
 * @author momosuke
 * @version 1.0
 */
public abstract class Sprite{
	
	Image c1Img, c2Img, rcImg, e1Img;
	int life;
	boolean left=false, right=false, up=true, onGraund=false, shot=false;
	boolean leftdirect=false, rightdirect=true;
	
	protected double x;
	protected double y;
	protected int w;
	protected int h;
	protected int[] G;
	protected Image Img;
	protected Map map;
	
	/**
	 * 構成要素
	 * @param x X座標
	 * @param y Y座標
	 * @param G 位置合わせ
	 * @param Img イメージ
	 * @param map マップ
	 */
	public Sprite(double x, double y, int[] G, Image Img, Map map){
		this.x = x;
		this.y = y;
		this.G = G;
		this.Img = Img;
		this.map = map;
		w=10;
		h=20;
	}
	
	/**
	 * 状態の更新
	 */
	public abstract void update();
	
	/**
	 * 衝突判定
	 */
	public boolean isCollision(Sprite sprite){
		Rectangle playerRect=new Rectangle(
			(int)x-G[0]/2, (int)y-G[1]/2, G[0], G[1]);
		Rectangle spriteRect=new Rectangle(
			(int)sprite.getX()-sprite.getW()/2, (int)sprite.getY()-sprite.getH()/2,
			 sprite.getW(), sprite.getH());
		if(playerRect.intersects(spriteRect)){
			return true;
		}
		return false;
	}
	
	/**
	 * X座標の取得
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Y座標の取得
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * 幅の取得
	 */
	public int getW(){
		return G[0];
	}
	
	/**
	 * 高さの取得
	 */
	public int getH(){
		return G[1];
	}
	
	/**
	 * 描画処理
	 */
	public void draw(Graphics g, int setX, int setY, ImageObserver observer){
		int sx=(int)x;
		int sy=(int)y;
		
		g.drawImage(Img, 
			sx+setX+G[6],       sy+setY+G[7],
			sx+setX+G[6]+G[0],  sy+setY+G[7]+G[1],
			G[2],               G[3],
			G[4],               G[5], observer);
		
	}
	
}
