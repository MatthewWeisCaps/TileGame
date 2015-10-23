package mweis.game.items;

import mweis.game.gfx.Image;
import mweis.game.level.tiles.Tile;

public abstract class Item {
	
	protected static enum Quality {
		GRAY(0), WHITE(1), GREEN(2), BLUE(3), PURPLE(4), ORANGE(5);
		
		final private static int[] COLOR = {0xB8B8B8, 0xFFFFFF, 0x3DFF3D, 0x4747FF, 0xB8008A, 0xF5B800};
		final private int rank;
		Quality(int rank){
			this.rank = rank;
		}
		
		final int getColor(){
			return COLOR[rank];
		}
		
		final int getRank(){
			return rank;
		}
	}
	
	
	public final int id, stackSize, sellPrice, buyPrice;
	public final String name, description;
	public final Image thumbnail, dropImage;
	public final Quality quality;
	
	private static Image testImage = new Image("/player_front.png");
	private static Image testImage2 = new Image("/player_back.png");
	
	public static final Item[] items = new Item[256];
	public static final BasicItem testItem = new BasicItem(0, "testItem1", "an item for testing", Quality.GRAY, new Image("/red_item.png"), new Image("/red_item.png"), 20, -1, -1);
	public static final BasicItem testItem2 = new BasicItem(1, "testItem2", "an item for testing", Quality.GRAY, new Image("/blue_item.png"), new Image("/blue_item.png"), 20, -1, -1);
	public static final BasicItem testItem3 = new BasicItem(2, "testItem3", "an item for testing", Quality.GRAY, new Image("/green_item.png"), new Image("/green_item.png"), 20, -1, -1);
	public static final BasicItem testItem4 = new BasicItem(3, "testItem4", "an item for testing", Quality.GRAY, new Image("/white_item.png"), new Image("/white_item.png"), 20, -1, -1);
	
	public Item(int id, String name, String description, Quality quality, Image thumbnail, Image dropImage, int stackSize, int sellPrice, int buyPrice){
		this.id = id;
		this.name = name;
		this.description = description;
		this.quality = quality;
		this.thumbnail = thumbnail;
		this.dropImage = dropImage;
		this.stackSize = stackSize;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
		
		// add item to array:
		if (items[id] != null)
			throw new RuntimeException("Duplicate item id on " + id);
		items[id] = this;
	}
	
	public abstract void onUse();
		
}