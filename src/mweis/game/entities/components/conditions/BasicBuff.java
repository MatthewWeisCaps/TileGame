package mweis.game.entities.components.conditions;

import mweis.game.entities.Entity;
import mweis.game.gfx.Image;
import mweis.game.gfx.Screen;

public class BasicBuff extends Buff {
		
	public BasicBuff() {
		super(new Image("/red_item.png"), 150);
	}

	@Override
	void onApply(Entity entity) {
		System.out.println("basic buff applied");
	}

	@Override
	void onRemove(Entity entity) {
		System.out.println("basic buff removed");
	}

	@Override
	void update(Entity entity) {
		
	}

	@Override
	void render(Entity entity, Screen screen) {
		
	}
	
}
