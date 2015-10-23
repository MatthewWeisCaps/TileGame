package mweis.game.gfx;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mweis.game.level.Level;

public class Light {
	// basically how far away things need to be for a shadow to draw on them
	// this should eventually be set to the light's image size
	public final static float GRADIENT_SIZE = 50f; // for shadows, this should be EQUAL to the size of our light images (might need to make light class that holds image + width)
	final float SHADOW_EXTRUDE;
	
	protected Rectangle bounds;
	protected Image image;
	
	boolean hasIlluminated = false;
	
	Point midpoint; // used when we draw to raster
	List<Polygon> shadows = new ArrayList<Polygon>(); // all shadows cast by this object
	List<Point> casts = new ArrayList<Point>(); // all blocks that cast shadows as a result of this lightsource
	
	
	public Light(int x, int y, Image image){
		bounds = new Rectangle();
		bounds.x = x;
		bounds.y = y;
		bounds.width = image.width;
		bounds.height = image.height;
		this.image = image;
		SHADOW_EXTRUDE = GRADIENT_SIZE*GRADIENT_SIZE;
		midpoint = new Point(x - bounds.width/2, y - bounds.height/2);
	}
	
	// render light to the shader
	public void render(Screen screen, Level level){
		if (!hasIlluminated || level.hasShifted()){
			illuminate(screen, level);
		}
		screen.lightShader.additive(midpoint.x - screen.ox, midpoint.y - screen.oy, screen.ox, screen.oy, image, shadows, casts);
	}
	
	// calculates the light's shadows and casts
	public void illuminate(Screen screen, Level level){
		
		reset(); // reset the shadows and casts
		
		// loop through the level:
			final int ox = screen.ox/level.tilesize;
			final int oy = screen.oy/level.tilesize;
			
			/** +2 has to do with the potential lost value from casting */
			final int width = Math.min(2+ox+screen.width/level.tilesize, level.width);
			final int height = Math.min(2+oy+screen.height/level.tilesize, level.height);
			
			final int length = level.width*level.height;
			
			float minDistSq = GRADIENT_SIZE*GRADIENT_SIZE;
			
			for (int y=oy; y < height; y++){
				for (int x=ox; x < width; x++){
					// exception thrown? that means we're calling a tileid that doesn't exist!
					if (x+y*level.width < 0 || x+y*level.width >= length)
						continue;
					
					// we are using "solid" as the condition to cast a light.. this is for testing.
					if (!level.getTile(x, y).isSolid)
						continue;
					
					/** now we start using the algorithm found in mattdesl's shadow gradient code example.. */
									
					//radius of Tile's bounding circle
					float r = (int)(level.tilesize/2f);
					//r *= 1; // so shadows cover a little more
									
					//get center of entity
					float cx = (float)x*level.tilesize + r;
					float cy = (float)y*level.tilesize + r;
									
					//get direction from light(player) to tile center
					float dx = cx - this.bounds.x;
					float dy = cy - this.bounds.y;
									
					//get euclidean distance from mouse to center
					float distSq = dx * dx + dy * dy; //avoid sqrt for performance
									
					//if the entity is outside of the shadow radius, then ignore
					if (distSq > minDistSq) 
						continue; 
									
					//normalize the direction to a unit vector
					float len = (float)Math.sqrt(distSq);
					float nx = dx;
					float ny = dy;
					if (len != 0) { //avoid division by 0
						nx /= len;
						ny /= len;
					}
					
					//get perpendicular of unit vector
					float px = -ny;
					float py = nx;
					
					
					//our perpendicular points in either direction from radius
					Point2D.Float A = new Point2D.Float(cx - px * r, cy - py * r);
					Point2D.Float B = new Point2D.Float(cx + px * r, cy + py * r);
					
					final Point2D.Float point = new Point2D.Float(bounds.x, bounds.y); // used when we calculate
					
					//project the points by our SHADOW_EXTRUDE amount
					Point2D.Float C = screen.lightShader.project(point, A, SHADOW_EXTRUDE);
					Point2D.Float D = screen.lightShader.project(point, B, SHADOW_EXTRUDE);
					
					Polygon shadow = new Polygon();
				
					//construct a shadow(polygon) from our points
					shadow.addPoint((int)A.x, (int)A.y);
					shadow.addPoint((int)B.x, (int)B.y);
					shadow.addPoint((int)D.x, (int)D.y);
					shadow.addPoint((int)C.x, (int)C.y);
					
					// add the shadow(polygon) to the array
					shadows.add(shadow);
					
					// add the block from which we casted to the array
					casts.add(new Point(x*level.tilesize, y*level.tilesize));
				}
			}
		hasIlluminated = true;
	}
	
	public void reset(){
		shadows.clear(); // clear all shadows in the array
		casts.clear(); // clear all casts in the array
		hasIlluminated = false; // tell the class we need to re-calculate shadows and casts before the next render
	}
	
	public void move(int x, int y){
		if (x > 0)
			this.bounds.x = x;
		if (y > 0)
			this.bounds.y = y;
		midpoint = new Point(x - bounds.width/2, y - bounds.height/2);
		hasIlluminated = false;
	}
}
