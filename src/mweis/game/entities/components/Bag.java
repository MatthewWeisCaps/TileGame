package mweis.game.entities.components;

import mweis.game.gfx.Image;
import mweis.game.items.Item;

public class Bag {
	
	public enum Graphics {
		BASICBAG(new Image("/player_front.png"));
		
		public final Image image;
		Graphics(Image image){
			this.image = image;
		}
	}
	
	private int[] items;
	final int size;
	final Image image;
	
	public Bag(int size, Image image){
		this.size = size;
		this.items = new int[this.size];
		// set all to -1, this means empty
		for (int i=0; i < this.size; i++){
			items[i] = -1;
		}
		
		this.image = image;
	}
	
	public Item getItem(int slot){
		try {
			if (items[slot] == -1)
				return null;
			return Item.items[items[slot]];
			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Unable to get item, pos: " + slot + " is out of bounds.");
			return null;
		}
	}
	
	public int getNextOpenSlot(){
		for (int i=0; i < size; i++){
			if (items[i] == -1){
				return i;
			}
		}
		return -1;
	}
	
	public void addItem(int slot, int id){
		try { 
			items[slot] = id;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Unable to add item, pos: " + slot + " is out of bounds.");
		}
	}
	
}
