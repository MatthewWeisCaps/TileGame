package mweis.game.level.tiles;

import mweis.game.gfx.Screen;

public class BasicTile extends Tile {

	
	public BasicTile(int id, boolean isSolid, String name, String path) {
		super(id, isSolid, name, path);
	}
	
	public void update(){
		
	}
	
	// where x and y are literal.
	public void render(int x, int y, Screen screen){
		screen.render(x, y, image);
	}
	
}
