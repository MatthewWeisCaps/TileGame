package mweis.game.entities.components.conditions;

import java.util.ArrayList;
import java.util.List;

import mweis.game.entities.Entity;
import mweis.game.gfx.Screen;

public class ConditionHandler {
	private Entity entity;
	
	// this class wraps Conditions with their time left
	private class ConditionWrapper
	{
		private Condition condition;
		private long ttl; // time to live.
		
		ConditionWrapper(Condition condition)
		{
			this.condition = condition;
			this.ttl = condition.getDuration();
		}
		public void update()
		{
			if (ttl-- > 0)
				condition.update(entity);
			else
				removeFlags.add(this);
		}
	}
	
	private List<ConditionWrapper> removeFlags = new ArrayList<ConditionWrapper>();
	protected List<ConditionWrapper> buffs = new ArrayList<ConditionWrapper>();
	protected List<ConditionWrapper> debuffs = new ArrayList<ConditionWrapper>();
	
	public ConditionHandler(Entity entity){
		this.entity = entity;
	}
	
	public void add(Buff buff){
		buffs.add(new ConditionWrapper(buff));
		buff.onApply(entity);
	}
	public void add(Debuff debuff){
		debuffs.add(new ConditionWrapper(debuff));
		debuff.onApply(entity);
	}
	public void remove(ConditionWrapper cw){
		cw.condition.onRemove(entity); // call [de]buff's removal code
		if (cw.condition.isBuff) // remove from list
		{
			buffs.remove(cw);
		}
		else
		{
			debuffs.remove(cw);
		}
	}
	
	public void update(){
		for (ConditionWrapper cw : buffs){
			cw.update();
		}
		for (ConditionWrapper cw : debuffs){
			cw.update();
		}
		
		// if flagged for removal
		for (ConditionWrapper cw : removeFlags) {
			remove(cw);
		}
		removeFlags.clear();
	}
	
	public void render(Screen screen){
		for (ConditionWrapper cw : buffs){
			cw.condition.render(entity, screen);
		}
		for (ConditionWrapper cw : debuffs){
			cw.condition.render(entity, screen);
		}
	}
}
