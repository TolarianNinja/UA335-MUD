package items;

import java.util.ArrayList;

import model.EquipLocation;
import model.Item;

public class ContainerItem extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> inventory;
	
	public ContainerItem(int vnum, String shortDesc, String longDesc, String roomDesc, EquipLocation eq) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		this.inventory = new ArrayList<Item>();
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}
}