package mweis.game.entities.components.conditions;

import mweis.game.entities.Entity;
import mweis.game.gfx.Image;
import mweis.game.gfx.Screen;


/** Keeps track of all conditions in a static context - much like tile or item. */
public abstract class Condition {
	
	final Image image;
	final boolean isBuff; // if yes - we have a buff, if no - we have a debuff.
	private final long duration; // if duration == 0, it lasts forever.
	
	public Condition(boolean isBuff, Image image, long duration) {
		this.image = image;
		this.isBuff = isBuff;
		this.duration = duration;
	}
	
	abstract void onApply(Entity entity);
	abstract void onRemove(Entity entity);
	abstract void update(Entity entity);
	abstract void render(Entity entity, Screen screen); // this is NOT to render the buff image, this is to render any effects the condition might have - such as making the player blue.
	
	public long getDuration()
	{
		return this.duration;
	}

}
