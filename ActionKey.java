
/**
 * キーの構成
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
	* ノーマルモード用
	*/
	public ActionKey(){
		this(NORMAL);
	}
	
	/**
	* モード指定用
	*/
	public ActionKey(int mode){
		this.mode=mode;
		reset();
	}
	
	/**
	* キーの状態をリセット
	*/
	public void reset(){
		state=STATE_RELEASED;
		amount=0;
	}
	
	/**
	* キーが押されたとき呼び出す
	*/
	public void press(){
		if(state!=STATE_WAITING){
			amount++;
			state=STATE_PRESSED;
		}
	}
	
	/**
	* キーが離されたとき呼び出す
	*/
	public void release(){
		state=STATE_RELEASED;
	}
	
	/**
	* キーが押されたか
	* @return 押されたらtrueを返す
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
