package mweis.game.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mweis.game.gfx.Screen;

public class LevelLoader {
	
	/**
	 * LEVEL - FILE FORMAT:
	 * WIDTH
	 * HEIGHT
	 * LEVEL DATA[] (seperated by commas)
	 * */
	
	int width = -1;
	int height = -1;
	int levelData[] = null;
	
	public LevelLoader(String path){
		BufferedReader br = null;
		int i=0;
		try {
			 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(path));
				
			while ((sCurrentLine = br.readLine()) != null) {
//				System.out.println(sCurrentLine);
				
				// loop through lines here
				if (width == -1){
					width = Integer.valueOf(sCurrentLine);
					continue;
				} else if (height == -1){
					height = Integer.valueOf(sCurrentLine);
					levelData = new int[width*height];
					continue;
				} else {
					List<String> level = Arrays.asList(sCurrentLine.split(","));
					for(String tile : level){
					    levelData[i++] = Integer.valueOf(tile);
					}
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
		
	}
	
	public Level getLevel(int tilesize, Screen screen){
		return new Level(width, height, tilesize, levelData, screen);
	}
	
	
}
