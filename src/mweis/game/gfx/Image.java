package mweis.game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import mweis.game.gfx.shaders.Shader;

public class Image {
	public final int NULL_PIXEL = 0x000000;
	public final int width, height, length;
	public final int pixels[];
	
	public Image(String path){
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			// error? don't forget .png!
			e.printStackTrace();
		}
		
		if (image == null){
			this.width = (Integer) null;
			this.height = (Integer) null;
			this.length = (Integer) null;
			this.pixels = null;
			return;
		}
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.length = width*height;
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		// rm alpha
		for (int i=0; i < pixels.length; i++){
			pixels[i] = pixels[i] & 0xffffff;
		}
	}
	
	/** Create image with from shader */
	public Image(Shader shader){
		BufferedImage image = new BufferedImage(shader.width, shader.height, BufferedImage.TYPE_INT_RGB);
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.length = width*height;
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width); // make pixels black
		
		// fill pixels
		for (int i=0; i < pixels.length; i++){
			pixels[i] = shader.pixels[i] & 0xffffff;
		}
	}
}
