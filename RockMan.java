import java.awt.*;
import javax.swing.*;

/**
 * ロックマン
 * @author momosuke
 * @version 1.0
 */
public class RockMan extends JFrame {

	/**
	 * 初期設定
	 */
	public RockMan() {
		
		// タイトルを設定
		setTitle("RockMan");
		
		// メインパネルを作成してフレームに追加
		Adv panel = new Adv();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		// パネルサイズに合わせてフレームサイズを自動設定
		pack();
		
	}
	
	/**
	 * ゲーム起動
	 */
	public static void main(String[] args) {
		RockMan frame = new RockMan();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
