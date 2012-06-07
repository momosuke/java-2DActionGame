
/**
 * �L�[�̍\��
 * @author momosuke
 * @version 1.0
 */
public class ActionKey{
	
	public static final int NORMAL=0;
	public static final int DETECT_PRESS=1;
	
	int STATE_RELEASED=0;
	int STATE_PRESSED=1;
	int STATE_WAITING=2;
	
	int mode;
	int amount;
	int state;
	
	/**
	* �m�[�}�����[�h�p
	*/
	public ActionKey(){
		this(NORMAL);
	}
	
	/**
	* ���[�h�w��p
	*/
	public ActionKey(int mode){
		this.mode=mode;
		reset();
	}
	
	/**
	* �L�[�̏�Ԃ����Z�b�g
	*/
	public void reset(){
		state=STATE_RELEASED;
		amount=0;
	}
	
	/**
	* �L�[�������ꂽ�Ƃ��Ăяo��
	*/
	public void press(){
		if(state!=STATE_WAITING){
			amount++;
			state=STATE_PRESSED;
		}
	}
	
	/**
	* �L�[�������ꂽ�Ƃ��Ăяo��
	*/
	public void release(){
		state=STATE_RELEASED;
	}
	
	/**
	* �L�[�������ꂽ��
	* @return �����ꂽ��true��Ԃ�
	*/
	public boolean isPressed(){
		if(amount!=0){
			if(state==STATE_RELEASED){
				amount=0;
				return false;
			}else if(mode==DETECT_PRESS){
				state=STATE_WAITING;
				amount=0;
			}
			return true;
		}
		return false;
	}
}
