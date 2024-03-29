import java.awt.*;
import java.awt.image.*;

/**
 * 弾の構成
 * @author momosuke
 * @version 1.0
 */
public class Bullet extends Sprite {
	double svx;
	
	/**
	 * 構成要素
	 * @param G 位置合わせ
	 * @param vx 弾の速度
	 */
	public Bullet(double x, double y, int[] G, Image Img, Map map, double vx){
		super(x, y, G, Img, map);
		rcImg = Img;
		svx=vx;
	}
	
	/**
	 * 弾の座標計算
	 */
	public void update(){
		x+=svx;
	}
	
}
