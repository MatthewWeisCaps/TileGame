package mweis.game.entities.components.conditions;

import mweis.game.entities.Entity;
import mweis.game.gfx.Image;

public abstract class Debuff extends Condition {
	
	public Debuff(Image image, long duration) {
		super(false, image, duration);
	}
	
}
