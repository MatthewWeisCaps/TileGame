package mweis.game.entities;

import mweis.game.entities.components.Bag;
import mweis.game.gfx.Screen;
import mweis.game.items.Item;
import mweis.game.level.Level;

/** This class is for items when they are on the ground. */
public class DroppedItem extends Entity {
	
	private final Item item;
	private int bob; // for the bobbing up and down animation
	
	public DroppedItem(int x, int y, Item item, Level level) {
		super(level);
		bounds.x = x;
		bounds.y = y;
		bounds.width = 6;
		bounds.height = 6;
		this.item = item;
	}
	
	@Override
	public void onPlayerCollision(Player player){ // send the entity that collided with this one
		final Bag bag = player.inventory.getNextOpenBag();
		if (bag != null){
			final int slot = bag.getNextOpenSlot();
			bag.addItem(slot, item.id);
			System.out.println("Picked up " + item.name + ", put it into " + slot);
			// remove item from ground
			level.removeEntity(this);
		} else {
			System.out.println("Unable to pick up " + item.name + ", bags too full.");
		}
	}
	
	@Override
	public void render(Screen screen) {
		screen.render(bounds.x, bounds.y+bob, item.thumbnail);
	}

	@Override
	public void update() {
		
		if (level.tick % 60 < 10){
			bob = 0;
		} else if (level.tick % 60 < 20){
			bob = 1;
		} else if (level.tick % 60 < 30){
			bob = 2;
		} else if (level.tick % 60 < 40){
			bob = 3;
		} else if (level.tick % 60 < 50){
			bob = 2;
		} else if (level.tick % 60 < 60){
			bob = 1;
		}
	}

}
