package model;

import items.ContainerItem;
import items.DrinkContainerItem;
import items.PortalItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import controller.AccountCollection;
import controller.Translator;
import controller.XMLReader;
import effects.StatEffect;

public class Game {
	private static HashMap<Integer, Room> rooms;
	private static HashMap<Integer, Mob> mobs;
	private static HashMap<Integer, Item> items;
	private static ArrayList<Player> players;
	private static GameTimer gameTimer;
	private static final short roundLength = 1000;

	public Game() {
		rooms = new HashMap<Integer, Room>();
		mobs = new HashMap<Integer, Mob>();
		items = new HashMap<Integer, Item>();
		players = new ArrayList<Player>();
		gameTimer = new GameTimer(roundLength);
		XMLReader.buildGame();
//		getItem(7).addEffect(new StatEffect(15, "energy"));
//		getItem(8).addEffect(new StatEffect(-15, "energy"));
	}

	public static void LoginPlayer(String charName, AccountCollection ac) {
		try {

			ArrayList<Player> p = ac.getPlayers();

			int index = ac.isPlayer(charName);
			System.out.println("index = " + index);

			if (index >= 0) {
				Player thePlayer = ac.getPlayerAt(index);
				players.add(thePlayer);
				thePlayer.move(thePlayer.getCurrentRoomVnum());
//				thePlayer.getCurrentRoom().getCharacters()
//						.put(charName, thePlayer);
			} else {
				Player player = new Player(charName);
				players.add(player);
//				player.getCurrentRoom().getCharacters().put(charName, player);
				player.move(player.getCurrentRoomVnum());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Room getRoom(int vnum) {
		return rooms.get(vnum);
	}
	
	public static Item getItem(int vnum) {
		return items.get(vnum);
	}
	
	public static Mob getMob(int vnum) {
		return mobs.get(vnum);
	}
	
	public GameTimer getTimer() {
		return gameTimer;
	}
	
	/**
	 * 
	 * @param charName
	 *            The player whose room is needed.
	 * @return the room charName is in
	 */
	public static Room getPlayerRoom(String charName) {
		int vnum = findPlayer(charName).getCurrentRoomVnum();
		return rooms.get(vnum);
	}
	
	/**
	 * 
	 * @param charName
	 *            The player whose inventory is needed
	 * @return The inventory of player charName
	 */
	public static ArrayList<Item> getPlayerInventory(String charName) {
		return findPlayer(charName).getInventory();
	}
	
	/**
	 * If the item named is in the player's inventory, return the item,
	 * otherwise return null
	 * 
	 * @param charName
	 *            player whose inventory is searched
	 * @param objectName
	 *            the name of the item looking for
	 * @return the item if it is in inventory.
	 */
	public static Item inPlayerInventory(String charName, String objectName) {
		return findPlayer(charName).inInventory(objectName);
	}
	
	/**
	 * If the item named is worn by the player, return the item, otherwise
	 * return null
	 * 
	 * @param charName
	 *            player whose inventory is searched
	 * @param objectName
	 *            the name of the item looking for
	 * @return the item if it is in player's equipment.
	 */
	public static Item inPlayerEquipment(String charName, String objectName) {
		return findPlayer(charName).isWearing(objectName);
	}
	
	/**
	 * 
	 * @param charName
	 *            the name of the player whose room is checked
	 * @return ArrayList of all players (no mobs) that are in the room with
	 *         charName
	 */
	public static ArrayList<Player> localPlayers(String charName) {
		ArrayList<Player> output = new ArrayList<Player>();
		for (Character c : getPlayerRoom(charName).getCharacters()) {
			if (c instanceof Player)
				output.add((Player) c);
		}
		return output;
	}
	
	/**
	 * 
	 * @param charName
	 *            Name of the character who is being checked
	 * @return ArrayList of all characters (mobs/players) that are in the room
	 *         with charName.
	 */
	public static ArrayList<Character> localCharacters(String charName) {
		return getPlayerRoom(charName).getCharacters();
	}
	
	/**
	 * 
	 * @param charName
	 *            the name of the player searching
	 * @param targetName
	 *            The target searched for
	 * @return The target as either a character or an object, whichever one
	 *         matches the name searched for
	 */
	public static Object findLocalTarget(String charName, String targetName) {
		Object output = findLocalCharacter(charName, targetName);
		if (output == null)
			output = findLocalItem(charName, targetName);
		return output;
	}
	
	/**
	 * 
	 * @param charName
	 *            name of the player searching
	 * @param targetName
	 *            name of the item searched for
	 * @return An item that is in the room with charName or in charName's
	 *         inventory
	 */
	public static Item findLocalItem(String charName, String targetName) {
		Item output = findRoomItem(charName, targetName);
		if (output == null)
			output = inPlayerInventory(charName, targetName);
		return output;
	}
	
	/**
	 * 
	 * @param charName
	 *            name of the player searching
	 * @param targetName
	 *            name of item searched for
	 * @return if the item is on the ground in the room, return the item.
	 */
	public static Item findRoomItem(String charName, String targetName) {
		for (Item ir : getPlayerRoom(charName).getItems()) {
			if (ir.getName().contains(targetName))
				return ir;
		}
		return null;
	}
	
	/**
	 * 
	 * @param charName
	 *            name of player searching
	 * @param targetName
	 *            name of character searched for
	 * @return if the character is in the room, return the character
	 */
	public static Character findLocalCharacter(String charName, String targetName) {
		for (Character c : getPlayerRoom(charName).getCharacters()) {
			if (c instanceof Player && c.getName().contains(Translator.upperFirst(targetName))) {
				return c;
			}
			if (c.getName().contains(targetName))
				return c;
		}
		return null;
	}
	
	/**
	 * 
	 * @param targetName
	 *            Player searched for
	 * @return If the player is online, return the player.
	 */
	public static Player findPlayer(String targetName) {
		for (Player p : getAllPlayers()) {
			if (p.getPlayerName().equals(targetName))
				return p;
		}
		return null;
	}
	
	public static void addItemToRoom(int room, int item) {
		rooms.get(room).getItems().add(getItem(item));
	}
	
	public static void addCharactertoRoom(int room, Character cha) {
		rooms.get(room).getCharacters().add(cha);
	}
	
	public static HashMap<Integer, Room> getAllRooms() {
		return rooms;
	}

	public static HashMap<Integer, Mob> getAllMobs() {
		return mobs;
	}

	public static HashMap<Integer, Item> getAllItems() {
		return items;
	}

	public static ArrayList<Player> getAllPlayers() {
		return players;
	}
}
