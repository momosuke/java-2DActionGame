import java.awt.*;
import javax.swing.*;

/**
 * ���b�N�}��
 * @author momosuke
 * @version 1.0
 */
public class RockMan extends JFrame {

	/**
	 * �����ݒ�
	 */
	public RockMan() {
		
		// �^�C�g����ݒ�
		setTitle("RockMan");
		
		// ���C���p�l�����쐬���ăt���[���ɒǉ�
		Adv panel = new Adv();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		// �p�l���T�C�Y�ɍ��킹�ăt���[���T�C�Y�������ݒ�
		pack();
		
	}
	
	/**
	 * �Q�[���N��
	 */
	public static void main(String[] args) {
		RockMan frame = new RockMan();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
