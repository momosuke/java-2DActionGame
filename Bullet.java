import java.awt.*;
import java.awt.image.*;

/**
 * ’e‚Ì\¬
 * @author momosuke
 * @version 1.0
 */
public class Bullet extends Sprite {
	double svx;
	
	/**
	 * \¬—v‘f
	 * @param G ˆÊ’u‡‚í‚¹
	 * @param vx ’e‚Ì‘¬“x
	 */
	public Bullet(double x, double y, int[] G, Image Img, Map map, double vx){
		super(x, y, G, Img, map);
		rcImg = Img;
		svx=vx;
	}
	
	/**
	 * ’e‚ÌÀ•WŒvZ
	 */
	public void update(){
		x+=svx;
	}
	
}
