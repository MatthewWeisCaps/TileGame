package mweis.game.entities.components;

public class Inventory {
	
	private static final int MAXBAGS = 5; // 4 + 1 static bag
	
	Bag pocket = new Bag(8, null); // perma-bag
	Bag bags[] = new Bag[MAXBAGS];
	public Inventory(Bag ... bags){
		addBag(0, pocket);
		int i=1;
		for (Bag bag : bags){
			addBag(i++, bag);
		}
	}
	
	public Bag getBag(int slot){
		try {
			return bags[slot];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Unable to get bag, slot: " + slot + " is out of bounds.");
			return null;
		}
	}
	
	// returns the next Bag with 1+ slots open
	public Bag getNextOpenBag(){
		for (Bag bag : bags){
			if (bag == null)
				continue;
			for (int i=0; i < bag.size; i++){
				if (bag.getItem(i) == null){
					return bag;
				}
			}
		}
		return null;
	}
	
	public void addBag(int slot, Bag bag){
		try {
			bags[slot] = bag;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Unable to get bag, slot: " + slot + " is out of bounds.");
		}
	}
	
	public void removeBag(int slot){
		try {
			bags[slot] = null;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Unable to get bag, slot: " + slot + " is out of bounds.");
		}
	}
	
}
