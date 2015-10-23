package mweis.game.gfx.shaders;

import java.awt.Point;
import java.awt.geom.Point2D;

import mweis.game.gfx.Image;
import mweis.game.gfx.Screen;

public class LightShader extends Shader {

	public LightShader(Screen screen){
		super(screen);
	}
	
	public LightShader(Shader shader){
		super(shader);
	}
	
	/** Renders shader to screen */
	@Override
	public void apply() {
		for (int y=0; y < height; y++){
			for (int x=0; x < width; x++){
				final int index = x+y*width;
				screen.pixels[index] = shade(index);
			}
		}
	}

	/** The lightShaders multiplication algorithm. */
	private int shade(int index) {
		
		final int screen_pixel = screen.pixels[index];
		
		final int screen_r = (screen_pixel >> 16) & 0xFF;
		final int screen_g = (screen_pixel >> 8) & 0xFF;
		final int screen_b = screen_pixel & 0xFF;
		
		final int shader_pixel = this.pixels[index];
		
		final int shader_r = (shader_pixel >> 16) & 0xFF;
		final int shader_g = (shader_pixel >> 8) & 0xFF;
		final int shader_b = shader_pixel & 0xFF;
		
		final int r = (screen_r*shader_r)/255;
		final int g = (screen_g*shader_g)/255;
		final int b = (screen_b*shader_b)/255;
		
		final int pixel = ((r << 16) + (g << 8) + b) & 0xFFFFFF;
		
		return pixel;
	}
	
	/** Projects a point from end along the vector (end - start) by the given scalar amount. */
	public Point2D.Float project(Point2D.Float start, Point2D.Float end, float scalar) {
		float dx = end.x - start.x;
		float dy = end.y - start.y;
		//euclidean length
		float len = (float)Math.sqrt(dx * dx + dy * dy);
		//normalize to unit vector
		if (len != 0) { //avoid division by 0
			dx /= len;
			dy /= len;
		}
		//multiply by scalar amount
		dx *= scalar;
		dy *= scalar;
		return new Point2D.Float(end.x + dx, end.y + dy);
	}
}
