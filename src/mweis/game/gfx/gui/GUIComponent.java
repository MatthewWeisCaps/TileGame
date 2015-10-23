package mweis.game.gfx.gui;

import java.awt.Rectangle;

public abstract class GUIComponent {
	
	Rectangle bounds; // bounds of this component, screen will not render here. If this component doesn't block a clear rectangle leave this null
	
	public GUIComponent(Rectangle bounds){
		this.bounds = bounds;
	}
	
	public abstract void update();
	public abstract void render();
}
