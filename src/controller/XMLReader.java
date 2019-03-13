package controller;

import items.ArmorItem;
import items.ContainerItem;
import items.DrinkContainerItem;
import items.EdibleItem;
import items.HackableItem;
import items.PortalItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mobs.ShopkeepMob;
import model.Direction;
import model.EquipLocation;
import model.Game;
import model.Item;
import model.Mob;
import model.Room;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import effects.BinaryEffect;
import effects.DrunkEffect;
import effects.Effect;
import effects.StatEffect;

public class XMLReader {
	private static final String xmlFilePath = "mudData.xml";

	public static void buildGame() {

		SAXBuilder saxBuilder = new SAXBuilder();
		File xmlFile = new File(xmlFilePath);

		try {
			Document doc = (Document) saxBuilder.build(xmlFile);
			Element rootElement = doc.getRootElement();

			buildRooms(rootElement);
			buildItems(rootElement);
			buildMobs(rootElement);
			buildResets(rootElement);

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	/**
	 * 
	 * @param root
	 *            the root element of the document
	 * 
	 * @description: builds the rooms from the XML file and inputs them into the
	 *               game
	 */
	private static void buildRooms(Element root) {
		List listElement = root.getChildren("room");

		for (Object o : listElement) {
			Element el = (Element) o;

			// input the description data
			int roomVnum = Integer.parseInt(el.getChildText("vnum"));
			String roomName = el.getChildText("name");
			String desc = el.getChildText("desc");

			// create the room object
			Room room = new Room(roomVnum, roomName, desc);

			// input and build the exits
			List exitList = el.getChildren("exits");
			for (Object o2 : exitList) {
				Element exit = (Element) o2;
				if (exit.getChildText("north") != null) {
					int north = Integer.parseInt(exit.getChildText("north"));
					room.addExit(Direction.NORTH, north);
				}
				if (exit.getChildText("east") != null) {
					int north = Integer.parseInt(exit.getChildText("east"));
					room.addExit(Direction.EAST, north);
				}
				if (exit.getChildText("south") != null) {
					int north = Integer.parseInt(exit.getChildText("south"));
					room.addExit(Direction.SOUTH, north);
				}
				if (exit.getChildText("west") != null) {
					int north = Integer.parseInt(exit.getChildText("west"));
					room.addExit(Direction.WEST, north);
				}
				if (exit.getChildText("up") != null) {
					int north = Integer.parseInt(exit.getChildText("up"));
					room.addExit(Direction.UP, north);
				}
				if (exit.getChildText("down") != null) {
					int north = Integer.parseInt(exit.getChildText("down"));
					room.addExit(Direction.DOWN, north);
				}
			}

			// add the room to the game
			Game.getAllRooms().put(roomVnum, room);
		}
	}

	/**
	 * 
	 * @param root
	 *            the root element of the document
	 * 
	 * @description: builds the items from the XML file and inputs them into the
	 *               game
	 */
	private static void buildItems(Element root) {
		List listElement = root.getChildren("item");

		for (Object o : listElement) {
			Element el = (Element) o;
			Item item;

			// input the data
			int itemVnum = Integer.parseInt(el.getChildText("vnum"));
			String itemType = el.getChildText("type");
			String shortDesc = el.getChildText("short");
			String longDesc = el.getChildText("long");
			String roomDesc = el.getChildText("room");
			String nameDesc = el.getChildText("name");
			String wear = el.getChildText("wear");

			// determine wear location
			EquipLocation eqLoc = parseWearLocation(wear);

			// determine type and create object
			switch (itemType) {
			case "armor":
				item = new ArmorItem(itemVnum, shortDesc, longDesc, roomDesc,
						eqLoc);
				break;
			case "container":
				item = new ContainerItem(itemVnum, shortDesc, longDesc,
						roomDesc, eqLoc);
				break;
			case "drink":
				String liquidColor = el.getChildText("liquidcolor");
				Double maxDrinks = Double.parseDouble(el
						.getChildText("maxdrinks"));
				if (maxDrinks > 0) {
					Double startingDrinks = Double.parseDouble(el
							.getChildText("startingdrinks"));
					item = new DrinkContainerItem(itemVnum, shortDesc,
							longDesc, roomDesc, eqLoc, liquidColor, maxDrinks,
							startingDrinks);
				} else {
					item = new DrinkContainerItem(itemVnum, shortDesc,
							longDesc, roomDesc, eqLoc, liquidColor);
				}
				break;
			case "edible":
				item = new EdibleItem(itemVnum, shortDesc, longDesc, roomDesc,
						eqLoc);
				break;
			case "portal":
				int portalDest = Integer.parseInt(el
						.getChildText("destination"));
				item = new PortalItem(itemVnum, shortDesc, longDesc, roomDesc,
						eqLoc, portalDest);
				break;
			case "hackable":
				String awardCreditParse = el.getChildText("creditaward");
				if (awardCreditParse != null) {
					int creditAward = Integer.parseInt(awardCreditParse);
					item = new HackableItem(itemVnum, shortDesc, longDesc,
							roomDesc, eqLoc, creditAward);
				} else {
					item = new HackableItem(itemVnum, shortDesc, longDesc,
							roomDesc, eqLoc);
					String awardItemParse = el.getChildText("itemaward");
					Scanner awardScan = new Scanner(awardItemParse);
					while (awardScan.hasNext()) {
						int awardVnum = Integer.parseInt(awardScan.next());
						((HackableItem) item).addAwardItem(Game
								.getItem(awardVnum));
					}
					awardScan.close();
				}
				break;
			default:
				item = new Item(itemVnum, shortDesc, longDesc, roomDesc, eqLoc);
			}

			//
			if (el.getChildText("unhackable") != null)
				item.setHackable(false);

			List effectList = el.getChildren("effect");
			for (Object ef : effectList) {
				Element elEffect = (Element) ef;
				Effect effect;

				short effectDuration = 0;
				int effectIntensity = 0;
				String effectType = elEffect.getChildText("type");
				String effectIntenInput = elEffect.getChildText("intensity");
				if (effectIntenInput != null)
					effectIntensity = Integer.parseInt(elEffect
							.getChildText("intensity"));
				String effectArg = elEffect.getChildText("arg");
				String effectDurInput = elEffect.getChildText("duration");
				if (effectDurInput != null) {
					effectDuration = Short.parseShort(effectDurInput);
				}
				switch (effectType) {
				case "stat":
					effect = new StatEffect(effectIntensity, effectArg);
					break;
				case "binary":
					effect = new BinaryEffect(effectIntensity, effectDuration);
					break;
				case "drunk":
					effect = new DrunkEffect(effectIntensity, effectDuration);
					break;
				default:
					effect = null;
					break;
				}
				if (effect != null)
					item.addEffect(effect);
			}

			Scanner nameInput = new Scanner(nameDesc);
			while (nameInput.hasNext())
				item.addName(nameInput.next());
			nameInput.close();

			// add the item to the game
			Game.getAllItems().put(itemVnum, item);
		}
	}

	/**
	 * 
	 * @param root
	 *            the root element of the document
	 * 
	 * @description: builds the mobs from the XML file and inputs them into the
	 *               game
	 */
	private static void buildMobs(Element root) {
		List listElement = root.getChildren("mob");

		for (Object o : listElement) {
			Element el = (Element) o;
			Mob mob;

			// input the description data
			int mobVnum = Integer.parseInt(el.getChildText("vnum"));
			String nameDesc = el.getChildText("name");
			String shortDesc = el.getChildText("short");
			String longDesc = el.getChildText("long");
			String roomDesc = el.getChildText("room");

			// find the mob type
			String mobType = el.getChildText("type");

			// create the mob of specified type
			if (mobType != null) {
				switch (mobType) {
				case "shopkeep":
					mob = new ShopkeepMob(mobVnum, longDesc, shortDesc,
							roomDesc);
					break;
				default:
					mob = new Mob(mobVnum, longDesc, shortDesc, roomDesc);
					break;
				}
			} else {
				mob = new Mob(mobVnum, longDesc, shortDesc, roomDesc);
			}

			if (el.getChildText("unhackable") != null)
				mob.setHackable(false);

			// add names
			Scanner nameInput = new Scanner(nameDesc);
			while (nameInput.hasNext())
				mob.addName(nameInput.next());
			nameInput.close();

			// add the room to the game
			Game.getAllMobs().put(mobVnum, mob);
		}
	}

	/**
	 * 
	 * @param root
	 *            the root element of the document
	 * 
	 * @description: builds the resets (default load locations) from the XML
	 *               file and inputs them into the game
	 */
	private static void buildResets(Element root) {
		List listElement = root.getChildren("roomreset");

		for (Object o : listElement) {
			Element el = (Element) o;

			// input room and item data
			int roomVnum = Integer.parseInt(el.getChildText("vnum"));
			String itemResets = el.getChildText("item");

			// add item resets
			Scanner inputItems = new Scanner(itemResets);
			while (inputItems.hasNext()) {
				int item = Integer.parseInt(inputItems.next());
				Game.addItemToRoom(roomVnum, item);
			}
			inputItems.close();

			// each mob reset has possible item spawns
			List mobElements = el.getChildren("mob");
			for (Object mo : mobElements) {
				Element elMob = (Element) mo;
				ArrayList<Integer> itemList = new ArrayList<Integer>();

				// there is no mob reset here, skip to next roomreset
				if (elMob.getChildText("vnum").equals(""))
					break;

				// input the data
				int mobVnum = Integer.parseInt(elMob.getChildText("vnum"));
				String mobItems = elMob.getChildText("item");
				String mobEquip = elMob.getChildText("wear");

				// get the mob from the master list
				Mob mob = Game.getMob(mobVnum);

				// get the vnum of each item
				Scanner mobItemScan = new Scanner(mobItems);
				while (mobItemScan.hasNext()) {
					itemList.add(Integer.parseInt(mobItemScan.next()));
				}
				mobItemScan.close();

				int itemCount = 0;
				Scanner mobEquipScan = new Scanner(mobEquip);
				while (mobEquipScan.hasNext()) {
					EquipLocation mEq = parseWearLocation(mobEquipScan.next());
					if (mEq == EquipLocation.INVENTORY) {
						mob.getInventory().add(
								Game.getItem(itemList.get(itemCount)));
					} else {
						mob.getEquipment().put(mEq,
								Game.getItem(itemList.get(itemCount)));
					}
					itemCount++;
				}
				mobEquipScan.close();
				Game.addCharactertoRoom(roomVnum, mob);
			}
		}
	}

	/**
	 * 
	 * @param wear
	 *            the string from the XML file corresponding to the
	 *            EquipLocation of the item
	 * @return the EquipLocation of to the string.
	 */
	private static EquipLocation parseWearLocation(String wear) {
		switch (wear) {
		case "inventory":
			return EquipLocation.INVENTORY;
		case "head":
			return EquipLocation.HEAD;
		case "face":
			return EquipLocation.FACE;
		case "torso":
			return EquipLocation.TORSO;
		case "arms":
			return EquipLocation.ARMS;
		case "onhands":
			return EquipLocation.ON_HANDS;
		case "waist":
			return EquipLocation.WAIST;
		case "legs":
			return EquipLocation.LEGS;
		case "feet":
			return EquipLocation.FEET;
		case "lefthand":
			return EquipLocation.HAND_LEFT;
		case "righthand":
			return EquipLocation.HAND_RIGHT;
		case "float":
			return EquipLocation.FLOATING;
		default:
			return null;
		}
	}
}
