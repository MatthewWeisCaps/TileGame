package mweis.game.entities;

import java.awt.Rectangle;

import mweis.game.gfx.Screen;
import mweis.game.level.Level;

public abstract class Entity {
	
	private static byte NEXT_ID = 0;
	
	public final byte id;
	protected Rectangle bounds;
	protected Level level;
	
	public Entity(Level level){
		this.id = NEXT_ID++;
		bounds = new Rectangle();
		this.level = level;
		level.addEntity(this);
	}
	
	public abstract void render(Screen screen);
	public abstract void update();
	
	// events (override as needed):
	public void onPlayerCollision(Player player){}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public int getX(){
		return bounds.x;
	}
	
	public int getY(){
		return bounds.y;
	}
	
	public int getWidth(){
		return bounds.width;
	}
	
	public int getHeight(){
		return bounds.height;
	}
	
}
