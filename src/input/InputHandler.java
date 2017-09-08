package input;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class InputHandler implements EventHandler<KeyEvent> {
	volatile String keys=" ";
	volatile String pressed = " ";
	@Override
	public void handle(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getEventType()==KeyEvent.KEY_PRESSED&&!keys.contains(" "+event.getCode().getName()+","))
			keys = keys+event.getCode().getName()+", ";
		else if(event.getEventType()==KeyEvent.KEY_RELEASED){
			keys = keys.replace(" "+event.getCode().getName()+",", "");
			pressed = pressed+event.getCode().getName()+", ";
		}
	}
	
	public String[] getkeys(){
		String[] ans = new String[]{keys,pressed};
		pressed = " ";
		return ans;
	}
	
	public String[] getkeysforConsol(){
		String[] ans = new String[]{keys,pressed};
		return ans;
	}

}
