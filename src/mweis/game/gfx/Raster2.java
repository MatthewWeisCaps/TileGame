package mweis.game.gfx;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import mweis.game.Game;

public class Raster2 {
	
	/** I wrote the raster code here before modding the main class. This class now serves no purpose, it's a copy of raster. */
	
	public final int width, height, length;
	protected int pixels[]; // should be protected!
	
	public Raster2(int width, int height){
		this.width = width;
		this.height = height;
		this.length = width*height;
		pixels = new int[length];
	}
	
	public int get(int i){
		return pixels[i];
	}
	
	public void multiply(int x, int y, Image image){
		multiply(x, y, image, null, null);
	}
	
	public void multiply(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts){
		/** Multiply class */
		class RenderClass extends RenderAlgorithm {
			@Override
			public int get(int pixel1, int pixel2) {
				final int r1 = (pixel1 >> 16) & 0xFF;
				final int g1 = (pixel1 >> 8) & 0xFF;
				final int b1 = pixel1 & 0xFF;

				final int r2 = (pixel2 >> 16) & 0xFF;
				final int g2 = (pixel2 >> 8) & 0xFF;
				final int b2 = pixel2 & 0xFF;

				final int r = (r1*r2)/255;
				final int g = (g1*g2)/255;
				final int b = (b1*b2)/255;
				
				final int pixel = ((r << 16) + (g << 8) + b) & 0xFFFFFF;
				return pixel;
			}
		}
		draw(x, y, image, shadows, casts, new RenderClass());
	}
	
	public void lighten(int x, int y, Image image){
		lighten(x, y, image, null, null);
	}
	
	public void lighten(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts){
		/** Multiply class */
		class RenderClass extends RenderAlgorithm {
			@Override
			public int get(int pixel1, int pixel2) {
				final int r1 = (pixel1 >> 16) & 0xFF;
				final int g1 = (pixel1 >> 8) & 0xFF;
				final int b1 = pixel1 & 0xFF;

				final int r2 = (pixel2 >> 16) & 0xFF;
				final int g2 = (pixel2 >> 8) & 0xFF;
				final int b2 = pixel2 & 0xFF;

				final int r = max(r1, r2);
				final int g = max(g1, g2);
				final int b = max(b1, b2);
				
				final int pixel = ((r << 16) + (g << 8) + b) & 0xFFFFFF;
				return pixel;
			}
		}
		draw(x, y, image, shadows, casts, new RenderClass());
	}

	public void darken(int x, int y, Image image){
		darken(x, y, image, null, null);
	}
	
	public void darken(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts){
		/** Multiply class */
		class RenderClass extends RenderAlgorithm {
			@Override
			public int get(int pixel1, int pixel2) {
				final int r1 = (pixel1 >> 16) & 0xFF;
				final int g1 = (pixel1 >> 8) & 0xFF;
				final int b1 = pixel1 & 0xFF;

				final int r2 = (pixel2 >> 16) & 0xFF;
				final int g2 = (pixel2 >> 8) & 0xFF;
				final int b2 = pixel2 & 0xFF;

				final int r = min(r1, r2);
				final int g = min(g1, g2);
				final int b = min(b1, b2);
				
				final int pixel = ((r << 16) + (g << 8) + b) & 0xFFFFFF;
				return pixel;
			}
		}
		draw(x, y, image, shadows, casts, new RenderClass());
	}
	
	public void additive(int x, int y, Image image){
		additive(x, y, image, null, null);
	}
	
	public void additive(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts){
		/** Multiply class */
		class RenderClass extends RenderAlgorithm {
			@Override
			public int get(int pixel1, int pixel2) {
				final int r1 = (pixel1 >> 16) & 0xFF;
				final int g1 = (pixel1 >> 8) & 0xFF;
				final int b1 = pixel1 & 0xFF;

				final int r2 = (pixel2 >> 16) & 0xFF;
				final int g2 = (pixel2 >> 8) & 0xFF;
				final int b2 = pixel2 & 0xFF;

				final int r = min(r1 + r2, 255);
				final int g = min(g1 + g2, 255);
				final int b = min(b1 + b2, 255);
				
				final int pixel = ((r << 16) + (g << 8) + b) & 0xFFFFFF;
				return pixel;
			}
		}
		draw(x, y, image, shadows, casts, new RenderClass());
	}
	
	// called by _multiply, _additive, etc
	private <RenderClass extends RenderAlgorithm> void draw(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts, RenderClass renderClass){
		int lit_x = x;
		int lit_y = y;
		
		try {
			// offset the shadows and casts
			for (Polygon shadow : shadows){
				shadow.translate(-x, -y);
			}
			
			for (Point2D.Float cast : casts){
				cast.x -= x;
				cast.y -= y;
			}
		
			x += Screen.center_x;
			y += Screen.center_y;
			
			final int w = image.width;
			final int start = x+y*width;
			
			for (int py = 0; py < image.height; py++){
				for (int px = 0; px < image.width; px++){
					
					boolean inCast = false; // assume we are not 
					if (casts != null){
						for (Point2D.Float cast : casts){
							// if our point is inside a shadow caster (which we DO want to draw)
							if ( (px >= cast.x && px <= cast.x + Game.TILESIZE) && (py >= cast.y && py <= cast.y + Game.TILESIZE) ){
								inCast = true;
								break;
							}
						}
					}
					if (shadows != null && !inCast){
						boolean didCol = false;
						for (Polygon shadow : shadows){
							if (shadow.contains(px, py)){
								final int pixel_index = start+px+py*width;
								
								if (pixel_index < 0 || pixel_index >= pixels.length)
									continue;
								
								//pixels[pixel_index] = 0xFFFFFF;
								didCol = true;
								break;
							}
						}
						if (didCol){
							continue;
						}
					}
					
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
						
					final int old_pixel = pixels[pixel_index];
					
					// send 2 pixels to out render algorithm
					pixels[pixel_index] = renderClass.get(old_pixel, image_pixel);
					
				}
			}
			
		} finally {
			// put the casts back (so we can re-use them next render-cycle if the entity hasn't moved)
			for (Polygon shadow : shadows){
				shadow.translate(lit_x, lit_y);
			}
			
			for (Point2D.Float cast : casts){
				cast.x += lit_x;
				cast.y += lit_y;
			}
		}
	}
	
	// all RenderMethods extend this class
	private abstract class RenderAlgorithm {
		public abstract int get(int pixel1, int pixel2);
	}
	
	
	/** Renders image to pixels | using an replacement algorithm | inserts the new pixel */
	public void render(int x, int y, Image image) {
		render(x, y, image, null, null);
	}
	
	public void render(int x, int y, Image image, List<Polygon> shadows, List<Point2D.Float> casts) {
		
		int lit_x = x;
		int lit_y = y;
		
		try {
			
			// offset the shadows and casts
			for (Polygon shadow : shadows){
				shadow.translate(-x, -y);
			}
			
			for (Point2D.Float cast : casts){
				cast.x -= x;
				cast.y -= y;
			}
		
			x += Screen.center_x;
			y += Screen.center_y;
			
			final int w = image.width;
			final int start = x+y*width;
			
			for (int py = 0; py < image.height; py++){
				for (int px = 0; px < image.width; px++){
					
					boolean inCast = false; // assume we are not 
					if (casts != null){
						for (Point2D.Float cast : casts){
							// if our point is inside a shadow caster (which we DO want to draw)
							if ( (px >= cast.x && px <= cast.x + Game.TILESIZE) && (py >= cast.y && py <= cast.y + Game.TILESIZE) ){
								inCast = true;
								break;
							}
						}
					}
					if (shadows != null && !inCast){
						boolean didCol = false;
						for (Polygon shadow : shadows){
							if (shadow.contains(px, py)){
								final int pixel_index = start+px+py*width;
								
								if (pixel_index < 0 || pixel_index >= pixels.length)
									continue;
								
								//pixels[pixel_index] = 0xFFFFFF;
								didCol = true;
								break;
							}
						}
						if (didCol){
							continue;
						}
					}
					
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
			
		} finally {
			// put the casts back (so we can re-use them next render-cycle if the entity hasn't moved)
			for (Polygon shadow : shadows){
				shadow.translate(lit_x, lit_y);
			}
			
			for (Point2D.Float cast : casts){
				cast.x += lit_x;
				cast.y += lit_y;
			}
		}
	}
	
	
	protected int max(int n1, int n2){
		if (n1 >= n2)
			return n1;
		else
			return n2;
	}
	
	protected int min(int n1, int n2){
		if (n1 <= n2)
			return n1;
		else
			return n2;
	}
}




