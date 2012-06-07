import java.awt.*;
import java.awt.image.*;

/**
 * �e�̍\��
 * @author momosuke
 * @version 1.0
 */
public class Bullet extends Sprite {
	double svx;
	
	/**
	 * �\���v�f
	 * @param G �ʒu���킹
	 * @param vx �e�̑��x
	 */
	public Bullet(double x, double y, int[] G, Image Img, Map map, double vx){
		super(x, y, G, Img, map);
		rcImg = Img;
		svx=vx;
	}
	
	/**
	 * �e�̍��W�v�Z
	 */
	public void update(){
		x+=svx;
	}
	
}
