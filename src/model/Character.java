package model;

/**
 * @author Alex Hartshorn
 * 
 * @description The character interface is a collection of shared traits between 
 * 				both mobs and players.  This will grow once we have all of the 
 * 				mechanics better figured out.
 * 
 */

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import controller.ServerOutputObject;
import feats.CharacterFeat;

public class Character implements Description, Serializable, Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8792063195696064643L;
	protected Room currentRoom;
	protected int currentRoomVnum;
	protected ArrayList<Item> inventory;
	protected HashMap<EquipLocation, Item> equipment;
	protected String longDesc;
	protected String shortDesc;
	protected String roomDesc;
	protected ArrayList<String> names;
	protected String charName;

	// added by Clint
	protected CharacterStatus charStatus;
	protected CharacterFeat charFeat;

	public Character(String longDesc, String shortDesc, String roomDesc) {
		this.charName = shortDesc;
		
		this.longDesc = longDesc;
		this.shortDesc = shortDesc;
		this.roomDesc = roomDesc;
		this.names = new ArrayList<String>();

		this.currentRoomVnum = 1;
		this.currentRoom = Game.getAllRooms().get(currentRoomVnum);
		this.inventory = new ArrayList<Item>();
		this.equipment = new HashMap<EquipLocation, Item>();
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	/**
	 * 
	 * @return the Vnum of the room that the character is in.
	 */
	public int getCurrentRoomVnum() {
		return currentRoomVnum;
	}

	public String getCharName() {
		return charName;
	}
	
	public void addName(String name) {
		names.add(name);
	}

	/**
	 * 
	 * @return the character's inventory. These are all the items the player is
	 *         holding but they are not currently wearing.
	 */
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public Item inInventory(String name) {
		for (Item item : getInventory()) {
			for (String iName : item.getName()) {
				if (iName.equals(name)) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return the character's equipment. This is a map that shows what the
	 *         person is wearing on their body. EquipLocation is the place on
	 *         their body that they are wearing something, and the item is what
	 *         they are wearing.
	 */
	public HashMap<EquipLocation, Item> getEquipment() {
		return equipment;
	}

	public void wearEquipment(String itemName) {
		Item inSlot = isWearing(itemName);
		Item selected = inInventory(itemName);
		if (inSlot != null) {
			removeEquipment(inSlot.getName().get(0));
		}
		if (selected != null) {
			equipment.put(selected.getWearLocation(), selected);
			inventory.remove(selected);
		}
	}

	public void removeEquipment(String itemName) {
		if (isWearing(itemName) != null) {
			inventory.add(isWearing(itemName));
			equipment.remove(isWearing(itemName).getWearLocation());
		}
	}

	public Item isWearing(String itemName) {
		for (EquipLocation eq : equipment.keySet()) {
			if (equipment.get(eq).getName().contains(itemName))
				return equipment.get(eq);
		}
		return null;
	}

	public Item getEquipAt(EquipLocation eq) {
		return equipment.get(eq);
	}

	public void move(int room) {
		currentRoom.getCharacters().remove(this);
		currentRoomVnum = room;
		currentRoom = Game.getAllRooms().get(room);
		currentRoom.getCharacters().add(this);
	}

	// write to console
	public void prompt() {

	}

	@Override
	public ArrayList<String> getName() {
		return names;
	}

	@Override
	public String getLong() {
		return longDesc;
	}

	@Override
	public String getShort() {
		return shortDesc;
	}

	@Override
	public String getRoom() {
		return roomDesc;
	}

	public CharacterStatus getCharStatus() {
		return charStatus;
	}
	
	public CharacterFeat getCharFeat() {
		return charFeat;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CharacterStatus && arg instanceof String) {
			String s = (String) arg;

			if (s.equals(charName)) {
				this.charStatus = (CharacterStatus) o;
				prompt();
			}
		}
	}
}