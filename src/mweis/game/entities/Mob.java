package mweis.game.entities;

import java.util.Timer;
import java.util.TimerTask;

import mweis.game.Game;
import mweis.game.entities.components.conditions.ConditionHandler;
import mweis.game.gfx.Image;
import mweis.game.level.Level;

public abstract class Mob extends Entity {
	
	private Timer movementTimer;
	private long speed = (long) (1000D/(Game.TILESIZE*5)); // 10 blocks per second.
	protected boolean hasMoved = false; // this needs to be set to false every update loop
	
	public ConditionHandler conditionHandler;
	
	public Mob(int width, int height, Level level) {
		super(level);
		bounds.width = width;
		bounds.height = height;
		
		conditionHandler = new ConditionHandler(this);
		
		// make the abstract movement function go off at a fixed rate.
		movementTimer = new Timer();// times in ms | delay: before 1st execution. period: time between tasks
		movementTimer.scheduleAtFixedRate( new TimerTask() {
			@Override
			public void run() {
				movement();
			}
		}, 0, speed);
	}
	
	abstract void movement(); // movement must be precise
	
	// where dx and dy are change in x
	protected void move(int dx, int dy){
		if (dx != 0 && dy != 0){
			move(dx, 0);
			move(0, dy);
		}
		
		if (!doesCollide(bounds.x+dx, bounds.y+dy)){
			hasMoved = true;
			bounds.x += dx;
			bounds.y += dy;
		}
	}
	
	private boolean doesCollide(int x, int y){
		
		/** Check level bounds */
		if (x < 0 || x+bounds.width >= level.literal_width){
			return true;
		}
		if (y < 0 || y+bounds.height >= level.literal_height){
			return true;
		}
		
		/** Check 4 edges */
		// horizontal
		for (int i=x; i < x+bounds.width; i++){
			// top
			if (level.getTile2(i, y).isSolid){
				return true;
			}
			// bot
			if (level.getTile2(i, y+bounds.height).isSolid){
				return true;
			}
		}
		
		// vertical
		for (int i=y; i < y+bounds.height; i++){
			// left
			if (level.getTile2(x, i).isSolid){
				return true;
			}
			// right
			if (level.getTile2(x+bounds.width, i).isSolid){
				return true;
			}
		}
		
		return false;
	}
	
	// in blocks per second
	protected void setSpeed(double speed){
		speed = (long)(1000D/(Game.TILESIZE*speed));
	}
	
	public double getSpeed(){
		return this.speed;
	}
	
	public int getMidpointX(){
		return super.bounds.x+bounds.width/2;
	}
	
	public int getMidpointY(){
		return super.bounds.y+bounds.height/2;
	}
	
	public boolean hasMoved(){
		return hasMoved;
	}
	
}
