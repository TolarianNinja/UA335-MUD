package model;

import java.util.ArrayList;

@Deprecated
public class ItemContainer {
	private ArrayList<Item> inventory;
	
	public ItemContainer() {
		this.inventory = new ArrayList<Item>();
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}
}