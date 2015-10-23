package mweis.game.entities.components.conditions;

import mweis.game.entities.Entity;
import mweis.game.gfx.Image;

public abstract class Buff extends Condition {

	public Buff(Image image, long duration) {
		super(true, image, duration);
	}

}