package mweis.game.items;

import mweis.game.gfx.Image;

public class BasicItem extends Item {

	public BasicItem(int id, String name, String description, Quality quality, Image thumbnail, Image dropImage, int stackSize, int sellPrice, int buyPrice) {
		super(id, name, description, quality, thumbnail, dropImage, stackSize, sellPrice, buyPrice);
	}

	@Override
	public void onUse() {
		System.out.println("BasicItem used.");
	}

}
