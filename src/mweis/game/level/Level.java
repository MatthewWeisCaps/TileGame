package mweis.game.level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import mweis.game.Game;
import mweis.game.entities.DroppedItem;
import mweis.game.entities.Entity;
import mweis.game.entities.Player;
import mweis.game.gfx.Screen;
import mweis.game.level.tiles.Tile;

public class Level {
	
	private Screen screen;
	
	public int tick = 0; // counting up
	public final int width, height, literal_width, literal_height, tilesize;
	public int[] level;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> removeQue = new ArrayList<Entity>(); // entites qued to be removed are put in here.
	private boolean hasShifted = false; // boolean tracks if the level has shifted this frame.
	
	// level does not save screen. it just centers it in the constructor
	public Level(int width, int height, int tilesize, int[] data, Screen screen){
		this.width = width;
		this.height = height;
		this.literal_width = width*tilesize;
		this.literal_height = height*tilesize;
		this.screen = screen;
		this.tilesize = tilesize;
		this.level = data;
		
		// put level in center of the map:
		if (literal_width < screen.width)
			Screen.center_x = (screen.width-literal_width)/2;
		
		if (literal_height < screen.height)
			Screen.center_y = (screen.height-literal_height)/2;
		
		
		// temp: level fill
//		for (int i=0; i < width*height; i++){
//			if (i < width){
//				level[i] = 1;
//				continue;
//			}
//			if (i > width*height-width){
//				level[i] = 1;
//				continue;
//			}
//			if (i % width == 0 || i % width == width-1){
//				level[i] = 1;
//				continue;
//			}
//		}
	}
	
	public void update(){
		hasShifted = false;
		
		for (Entity e : getEntities()){
			e.update();
		}
		
		for (Tile t : Tile.tiles){
			if (t == null)
				break;
			t.update();
		}
		
		removeQuedEntities();
		
		tick++;
	}
	
	public void render(){
		
		// level offsets:
		final int ox = screen.ox/tilesize;
		final int oy = screen.oy/tilesize;
		
		/** +2 has to do with the potential lost value from casting */
		final int width = Math.min(2+ox+screen.width/tilesize, this.width);
		final int height = Math.min(2+oy+screen.height/tilesize, this.height);
		
		// draw the level
		for (int y=oy; y < height; y++){
			for (int x=ox; x < width; x++){
				// exception thrown? that means we're calling a tileid that doesn't exist!
				if (x+y*this.width < 0 || x+y*this.width >= level.length)
					continue;
				getTile(x, y).render(x*tilesize, y*tilesize, screen);
			}
		}
		
		// draw entities
		
		for (Entity e : getEntities()){
			e.render(screen);
		}
	}
	
	
	
	/** lx and ly are level x and y, not literal */
	public Tile getTile(int lx, int ly){
		// exception thrown? that means we're calling a tileid that doesn't exist or we're calling a level[] that doesn't exist.
		return Tile.tiles[level[lx+ly*width]];
	}
	
	/** x and y are literal */
	public Tile getTile2(int x, int y){
		// exception thrown? that means we're calling a tileid that doesn't exist or we're calling a level[] that doesn't exist.
		x /= tilesize;
		y /= tilesize;
		return Tile.tiles[level[x+y*width]];
	}
	
	public void setTile(int lx, int ly, int newTile){
		hasShifted = true;
		// exception thrown? that means we're calling a tileid that doesn't exist or we're calling a level[] that doesn't exist.
		level[lx+ly*width] = newTile;
	}
	
	public void setTile2(int x, int y, int newTile){
		hasShifted = true;
		// exception thrown? that means we're calling a tileid that doesn't exist or we're calling a level[] that doesn't exist.
		x /= tilesize;
		y /= tilesize;
		level[x+y*width] = newTile;
	}
	
	public boolean hasShifted(){
		return hasShifted;
	}
	
	final public List<Entity> getEntitiesInBounds(Rectangle bounds){
		List<Entity> ie = new ArrayList<Entity>();
		for (Entity entity : entities){
			// collision detection
			if (bounds.intersects(entity.getBounds())){
				ie.add(entity);
			}
		}
		return ie;
	}
	
	// will set offset to midpoint (x, y)
	public void setOffsetMidpoint(int x, int y){
		if (x < this.literal_width - screen.width/2){
			screen.setOffset(x-screen.width/2, -1);
		}
		if (y < this.literal_height - screen.height/2){
			screen.setOffset(-1, y-screen.height/2);
		}
	}
	
	final public synchronized List<Entity> getEntities() {
        return this.entities;
    }
	
    final public synchronized void addEntity(Entity e){
		this.getEntities().add(e);
	}
    
    // adds entity to remove que. we can't remove entity mid update or the loop will crash.
    final public synchronized void removeEntity(Entity e){
    	removeQue.add(e);
    }
	
    // removes entities from the array via the removeQue. This should only be called at the end of each update.
    final private synchronized void removeQuedEntities(){
    	// remove the entities:
    	for (Entity entity : removeQue){
    		entities.remove(entity);
    	}
    	// clear the que:
    	removeQue.clear();
    }
}



