package mweis.game.level.tiles;

import mweis.game.gfx.Image;
import mweis.game.gfx.Screen;

public abstract class Tile {
	
	public final int id;
	public final boolean isSolid;
	public final String name;
	protected final Image image;
	
	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicTile(0, false, "VOID", "/VOID.png");
	public static final Tile GRASS = new BasicTile(1, true, "GRASS", "/Grass.png");
	
	public Tile(int id, boolean isSolid, String name, String path){
		this.id = id;
		this.isSolid = isSolid;
		this.name = name;
		this.image = new Image(path); // creating the image every time we make a tile may be slow.
		
		// add tile to array:
		if (tiles[id] != null)
			throw new RuntimeException("Duplicate tile id on " + id);
		tiles[id] = this;
	}
	
	public abstract void render(int x, int y, Screen screen);
	public abstract void update();
	
}
