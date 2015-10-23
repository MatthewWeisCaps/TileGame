package mweis.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
	
	// add keyListener to main game class.
	public InputHandler(Game game){
		game.addKeyListener(this); 
	}
	
	public class Key {
		private boolean pressed = false;
		
		public boolean isPressed(){
			return pressed;
		}
		
		public void toggle(boolean isPressed){
			pressed = isPressed;
		}
	}
	
	
	// objects of the class
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public Key one = new Key();
	public Key two = new Key();
	public Key three = new Key();
	public Key four = new Key();
	
	public Key action = new Key();
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP){ 
			up.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN){ 
			down.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT){ 
			left.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT){ 
			right.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_E){ 
			action.toggle(isPressed);
		}
		
		
		if(keyCode == KeyEvent.VK_1){ 
			one.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_2){ 
			two.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_3){ 
			three.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_4){ 
			four.toggle(isPressed);
		}
	}
}