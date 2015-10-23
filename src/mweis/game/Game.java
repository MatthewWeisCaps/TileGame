package mweis.game;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import mweis.game.entities.DroppedItem;
import mweis.game.entities.Player;
import mweis.game.entities.components.conditions.BasicBuff;
import mweis.game.gfx.Image;
import mweis.game.gfx.Screen;
import mweis.game.gfx.shaders.LightShader;
import mweis.game.items.Item;
import mweis.game.level.Level;
import mweis.game.level.LevelLoader;

public class Game extends Canvas implements Runnable {
	
	public static int WIDTH = 320;
	public static int HEIGHT = WIDTH / 16 * 9;
	public static int SCALE = 3;
	public final static int TILESIZE = 8;
	private final static double TICKS_PER_SECOND = 30D;
		
	private boolean running = false;
	private Thread thread;
	
	private final JFrame frame;
	private InputHandler input;
	
	private Screen screen;
	
	private Level level;
	private Player player;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public Game(){
		// setup the frame and canvas:
		frame = new JFrame();
		super.setPreferredSize(new java.awt.Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		frame.setResizable(false);
		frame.setTitle("Game");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		start();
	}
	
	public void update() {
		level.update();
	}
	
	public void render() { // all classes that render will just have to have a graphics2d arg
		BufferStrategy bs = super.getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3); // 3 was causing screen tearing
			return;
		}
		
		// when enabling / disabling a light, make sure to turn on/off the Light declared on the player.
		screen.clear();
		screen.lightShader.clear();
		level.render();
		if (input.action.isPressed()){
			screen.lightShader.apply();
		}
		// set raster pixels equal to screen pixels
		for (int i=0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		
		// render image to the canvas
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {		
		input = new InputHandler(this);
		screen = new Screen(WIDTH, HEIGHT);
		level = new LevelLoader("res/level.txt").getLevel(TILESIZE, screen);//new Level(30, 30, TILESIZE, screen);
		player = new Player(2*5, 2*5, input, level);
		
		
		//BasicBuff buff = new BasicBuff();
		player.conditionHandler.add(new BasicBuff()); // need to add conditionHandler (and maybe entity) as var for buffs
		// want to do: new BasicBuff(player.conditionHandler), and have it auto-add that way!
		
		// add in some test items
		new DroppedItem(25, 15, Item.testItem, level);
		new DroppedItem(45, 15, Item.testItem2, level);
		new DroppedItem(30, 45, Item.testItem3, level);
		new DroppedItem(30, 75, Item.testItem4, level);
	}
	
	public void run() {
		init();
		
		int updates = 0;
		int frames = 0;
		
		final double nsPerTick = 1000000000D/TICKS_PER_SECOND;
		double lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis(); // for counting every second.
		double delta = 0;
		
		while (running){
			
			double now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta > 1){
				updates++;
				update();
				delta -= 1;
			}
			
			frames++;
			render();
			
			if(System.currentTimeMillis() - lastTimer > 1000){ // 1 s is 1000 ms
				lastTimer += 1000;
				frame.setTitle(updates+" ticks, "+frames+" frames");
				frames = 0;
				updates = 0;
			}
		}
	}
	
	
	public static void main(String[] args){
		new Game();
	}
	
}
