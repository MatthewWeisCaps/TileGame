package mweis.game.gfx.gui;

import java.awt.Rectangle;

// example GUI component
public class ExampleGUIComponent extends GUIComponent {
	
	/** Specific code goes in each subclass, such as a action bar and the invidivual action images inside.*/
	// in this component, we might want width to always be 50, and height to always be 20, so we call super like this
	public ExampleGUIComponent(int x, int y) {
		super(new Rectangle(x, y, 50, 20));
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

}
