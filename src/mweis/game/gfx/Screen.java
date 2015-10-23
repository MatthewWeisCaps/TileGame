package mweis.game.gfx;

import mweis.game.gfx.shaders.LightShader;
import mweis.game.gfx.shaders.Shader;

public class Screen {
	
	// screen doesn't extend raster. too many differences for that.
	public final int width, height;
	public int ox = 0, oy = 0;
	public static int center_x = 0, center_y = 0;
	
	public int[] pixels;
	
	public LightShader lightShader;
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
		lightShader = new LightShader(this);
	}
	
	public void render(int x, int y, Image image){
		x += center_x;
		y += center_y;
		
		x -= ox;
		y -= oy;
		
		final int w = image.width;
		final int start = x+y*width;
		
		for (int py = 0; py < image.height; py++){
			for (int px = 0; px < image.width; px++){
				
				final int image_index = px+py*w;
				final int image_pixel = image.pixels[image_index];
				
				if (image_pixel == image.NULL_PIXEL)
					continue;
				
				// bounds check
				final int rx = x+px;
				final int ry = y+py;
				
				if (rx >= width || rx < 0 || ry >= height || ry < 0)
					continue;
				
				final int pixel_index = start+px+py*width;
				
				if (pixel_index < 0 || pixel_index >= pixels.length)
					continue;
				
				pixels[pixel_index] = image_pixel;
				
			}
		}
	}
	
	public void clear(){
		for (int i=0; i < pixels.length; i++){
			pixels[i] = 0x000000;
		}
	}
	
	// use -1 to keep the same. sets the offset to (x, y)
	public void setOffset(int x, int y){
		if (x > 0)
			ox = x;
		if (y > 0)
			oy = y;
	}
	
	// use 0 to keep the same. moves the offset by the specified value
	public void moveOffset(int x, int y){
		if (ox+x > 0)
			ox += x;
		if (oy+y > 0)
			oy += y;
	}
}
