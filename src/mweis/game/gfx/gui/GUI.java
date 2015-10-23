package mweis.game.gfx.gui;

import java.awt.Rectangle;

import mweis.game.gfx.Screen;

public class GUI {
	private Screen screen;
	
	private Rectangle bounds[]; // array that holds all areas the GUI draws, we don't have to render the screen here, which saves time.
	
	public GUI(Screen screen){
		this.screen = screen;
	}
	
	public void update(){
		
	}
	
	public void render(){
		
	}
	
}
