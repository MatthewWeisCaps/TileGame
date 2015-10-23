package mweis.game.gfx.shaders;

import java.awt.geom.Point2D;

import mweis.game.gfx.Image;
import mweis.game.gfx.Raster;
import mweis.game.gfx.Screen;

public abstract class Shader extends Raster {
	
	protected Screen screen;
	
	public Shader(Screen screen){
		super(screen.width, screen.height);
		this.screen = screen;
		clear();
	}
	
	// makes a new shader eq to an old one. We do this in constructor so we can edit the new and preserve the old
	public Shader(Shader shader){
		super(shader.screen.width, shader.screen.height);
		this.screen = shader.screen;
		// replicate pixels
		for (int i=0; i < length; i++){
			pixels[i] = shader.pixels[i];
		}
	}
	
	// create shader by combining shaders | saftey: checks if shaders share a screen
	public Shader(boolean safeMode, Shader ... shaders){
		super(shaders[0].screen.width, shaders[0].screen.height);
		
		// check if all shaders share a screen
		screen = shaders[0].screen;
		if (safeMode){
			for (Shader shader : shaders){
				if (screen != shader.screen){
					screen = null;
					throw new RuntimeException("Attempted to join 2 shaders that don't share the same screen");
				}
			}
		}
		
		// create the new shader using a max algorithm
		for (int i=0; i < length; i++){
			int max = 0;
			for (Shader shader : shaders){
				final int pixel = shader.pixels[i];
				if (pixel > max){
					max = pixel;
				}
			}
			pixels[i] = max;
		}
	}
	
	public abstract void apply(); // applys shader to screen
	
	public void clear(){
		for (int i=0; i < pixels.length; i++){
			pixels[i] = 0x000000;
		}
	}
}
