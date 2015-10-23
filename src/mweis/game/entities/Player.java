package mweis.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mweis.game.InputHandler;
import mweis.game.entities.components.Inventory;
import mweis.game.gfx.Image;
import mweis.game.gfx.Light;
import mweis.game.gfx.Screen;
import mweis.game.gfx.shaders.LightShader;
import mweis.game.level.Level;

public class Player extends Mob {
	
	Image image = Graphics.FRONT.image[0];
	
	Inventory inventory;
	
	private Light light;
	
	protected boolean key_up = false;
	protected boolean key_down = false;
	protected boolean key_left = false;
	protected boolean key_right = false;
	
	public static enum Graphics {
		FRONT("/player_front.png", "/player_front2.png"), BACK("/player_back.png", "/player_back2.png"),
		LEFT("/side1_left.png", "/side2_left.png"),RIGHT("/player_side1.png", "/player_side2.png"),
		LIGHT("/blue_light.png");
		
		public static final int OX = 3;
		public static final int OY = 3;
		public final Image[] image;
		Graphics(String... path){
			image = new Image[path.length];
			int i=0;
			for (String string : path){
				image[i++] = new Image(string);
			}
		}
	}
		
	protected InputHandler input;
	
	public Player(int x, int y, InputHandler input, Level level) {
		super(10, 10, level);
		this.bounds.x = x;
		this.bounds.y = y;
		this.input = input;
		light = new Light(getMidpointX(), getMidpointY(), Graphics.LIGHT.image[0]);
		
		inventory = new Inventory();
	}

	public void render(Screen screen) {
		screen.render((int)(bounds.x-Graphics.OX), (int)(bounds.y-Graphics.OY), image);
		conditionHandler.render(screen);
		
		/** Check if level or entity has moved - if not we can re-use shadows from the last frame. */
		if (level.hasShifted() || hasMoved()){
			light.illuminate(screen, level);
		}
		light.render(screen, level);
	}
	
	public void update() {
		conditionHandler.update();
		
		if (hasMoved){
			light.move(getMidpointX(), getMidpointY());
		}
		
		hasMoved = false; // reset each frame.
		
		key_up = false;
		key_down = false;
		key_left = false;
		key_right = false;
		
		if (input.left.isPressed()){
			key_left = true;
			if (level.tick % 20 < 10){
				image = Graphics.LEFT.image[0];
			} else {
				image = Graphics.LEFT.image[1];
			}
		}
		
		if (input.right.isPressed()){
			key_right = true;
			if (level.tick % 20 < 10){
				image = Graphics.RIGHT.image[0];
			} else {
				image = Graphics.RIGHT.image[1];
			}
		}
		if (input.up.isPressed()){
			key_up = true;
			if (level.tick % 20 < 10){
				image = Graphics.BACK.image[0];
			} else {
				image = Graphics.BACK.image[1];
			}
		}
			
		if (input.down.isPressed()){
			key_down = true;
			if (level.tick % 20 < 10){
				image = Graphics.FRONT.image[0];
			} else {
				image = Graphics.FRONT.image[1];
			}
		}
		
		
		// entity collision
		final List<Entity> collisions = level.getEntitiesInBounds(bounds);
		for (Entity entity : collisions){
			entity.onPlayerCollision(this);
		}
	}
	
	
	synchronized void movement(){
		// movement function
		//-1, 0
		if (key_left)
			move(-1, 0);
		
		if (key_right)
			move(1, 0);
		
		if (key_up)
			move(0, -1);
			
		if (key_down)
			move(0, 1);
		
		int x = getMidpointX();
		int y = getMidpointY();
		
		level.setOffsetMidpoint(x, y);
	}
}



