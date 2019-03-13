package items;

import java.awt.Color;
import java.util.ArrayList;

import controller.ServerOutputObject;
import controller.Translator;

import model.EquipLocation;
import model.Game;
import model.Item;

public class HackableItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1615659398752744435L;
	private int creditAward;
	private ArrayList<Item> hackableItems;
	private Item freeItem;

	public HackableItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		hackableItems = new ArrayList<Item>();
		this.creditAward = 0;
	}

	public HackableItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq, int creditAward) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		hackableItems = new ArrayList<Item>();
		this.creditAward = creditAward;
	}
	
	public void addAwardItem(Item award) {
		hackableItems.add(award);
	}

	public void use(String charName) {
		setHacked(true);
		if (!hackableItems.isEmpty()) {
			freeItem = findFreeItem();
			Game.getPlayerInventory(charName).add(freeItem);
		} else {
			Game.findPlayer(charName).getCharStatus().changeWorth(creditAward);
		}
	}

	private Item findFreeItem() {
		int selection = (int) (Math.random() * hackableItems.size());
		return hackableItems.get(selection);
	}

	@Override
	public ServerOutputObject hackString() {
		if (creditAward == 0 && hackableItems.isEmpty()) {
			return new ServerOutputObject(
					"This console doesn't have anything for you, hacker.",
					Color.WHITE, 1);
		}
		if (creditAward > 0) {
			return new ServerOutputObject("Your hacking prowess earned you "
					+ creditAward + " credits.", Color.WHITE, 1);
		} else if (freeItem != null) {
			return new ServerOutputObject(Translator.upperFirst(getShort())
					+ " opens up and gives you " + freeItem.getShort() + ".",
					Color.WHITE, 1);
		} else {
			return null;
		}
	}
}
