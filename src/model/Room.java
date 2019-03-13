package model;

/**
 * @author Alex Hartshorn
 * 
 * @description All of the information that needs to be stored for a 
 * 				room.  The exits each correspond
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Room implements Vnum, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4754159874846198258L;
	private int vnum;
	private String name;
	private String description;
	private HashMap<Direction, Integer> exits;
	private ArrayList<Character> characters;
	private ArrayList<Item> items;

	/**
	 * 
	 * @param vnum
	 *            the vnum of the room
	 * @param name
	 *            the name of the room
	 * @param description
	 *            the description of the room
	 */
	public Room(int vnum, String name, String description) {
		this.vnum = vnum;
		this.name = name;
		this.description = description;

		this.exits = new HashMap<Direction, Integer>();
		this.characters = new ArrayList<Character>();
		this.items = new ArrayList<Item>();
	}

	/**
	 * @return the vnum of the room
	 */
	public int getVnum() {
		return vnum;
	}

	/**
	 * 
	 * @return the name of the room
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the description of the game
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return all of the exits in the room
	 */
	public HashMap<Direction, Integer> getExits() {
		return exits;
	}
	
	public String exitsToString() {
		String output = "[ Exits: ";
		for (Direction direction : exits.keySet()) {
			switch(direction) {
			case DOWN:
				output += "down";
				break;
			case EAST:
				output += "east";
				break;
			case NORTH:
				output += "north";
				break;
			case SOUTH:
				output += "south";
				break;
			case UP:
				output += "up";
				break;
			case WEST:
				output += "west";
				break;
			default:
				output += "none";
				break;
			}
			output += " ";
		}
		return output + "]";
	}

	/**
	 * 
	 * @return all of the items that are in the room
	 */
	public ArrayList<Item> getItems() {
		return items;
	}

	/**
	 * 
	 * @return all of the characters in the room
	 */
	public ArrayList<Character> getCharacters() {
		return characters;
	}

	/**
	 * 
	 * @param direction
	 *            the direction the exit will go to
	 * @param nextRoom
	 *            the vnum of the room the direction will go to
	 */
	public void addExit(Direction direction, int nextRoom) {
		exits.put(direction, nextRoom);
	}
	
	public Item getValidItem(String itemName) {
		for (Item item : getItems()) {
			for (String name : item.getName()) {
				if (name.toLowerCase().equals(itemName))
					return item;
			}
		}
		return null;
	}
	
	public Character getValidCharacter(String charName) {
		for (Character c : getCharacters()) {
			for (String name : c.getName()) {
				if (name.toLowerCase().equals(charName))
					return c;
			}
		}
		return null;
	}
}