package command;

import java.awt.Color;

import mobs.ShopkeepMob;
import model.Character;
import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;

public class ShopCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3745475024326596948L;
	private ShopkeepMob shop;
	private Item transactionItem;
	private int transactionPrice;
	private boolean isBuying = false;
	private boolean isSelling = false;

	public ShopCommand(String charName) {
		super(charName);

	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			shop = (ShopkeepMob) findShop(charName);
		}
		if (shop != null) {
			if (getArgs().size() == 2) {
				if (getArgs().get(0).equals("buy")) {
					transactionItem = shop.inInventory(getArgs().get(1));
					if (transactionItem != null) {
						server.sendToRoom(charName, outputRoomBuy());
						transactionPrice = findPrice(transactionItem, false);
						isBuying = true;
					}
				} else if (getArgs().get(0).equals("sell")) {
					transactionItem = Game.findPlayer(charName).inInventory(
							getArgs().get(1));
					if (transactionItem != null) {
						server.sendToRoom(charName, outputRoomSell());
						transactionPrice = findPrice(transactionItem, true);
						isSelling = true;
					}
				}
			}
		}
		server.sendToUser(charName, outputUser());
		if (isSelling) {
			sellItem();
		} if (isBuying) {
			buyItem();
		}
	}

	private void sellItem() {
		Game.findPlayer(charName).getCharStatus().changeWorth(findPrice(transactionItem, true));
		Game.getPlayerInventory(charName).remove(transactionItem);
		shop.getInventory().add(transactionItem);
	}

	private void buyItem() {
		Game.findPlayer(charName).getCharStatus().changeWorth(-findPrice(transactionItem, false));
		Game.getPlayerInventory(charName).add(transactionItem);
	}

	private ServerOutputObject outputUser() {
		if (shop == null) {
			return new ServerOutputObject("There is no shop here.",
					Color.WHITE, 1);
		}
		if (getArgs().size() == 0) {
			return new ServerOutputObject(
					"shop list\nshop buy <item>\nshop sell <item>",
					Color.WHITE, 1);
		} else if (getArgs().size() == 1) {
			if (getArgs().get(0).equals("buy"))
				return new ServerOutputObject(
						"Buy what? Check the shop list to see what's for sale.",
						Color.WHITE, 1);
			else if (getArgs().get(0).equals("sell"))
				return new ServerOutputObject("Sell what?", Color.WHITE, 1);
			else if (getArgs().get(0).equals("list")) {
				return new ServerOutputObject(shopList(), Color.WHITE, 1);
			} else
				return new ServerOutputObject(
						"shop list\nshop buy <item>\nshop sell <item>",
						Color.WHITE, 1);
		} else if (getArgs().size() == 2) {
			if (getArgs().get(0).equals("buy")) {
				if (transactionItem == null) {
					return new ServerOutputObject(Translator.upperFirst(shop
							.getShort()) + " is not selling that.",
							Color.WHITE, 1);
				} else if (!canBuy()) {
					return new ServerOutputObject("You can't afford that.", Color.WHITE, 1);
				} else {
					return new ServerOutputObject("You buy "
							+ transactionItem.getShort() + " for "
							+ transactionPrice + " credits.", Color.WHITE, 1);
				}
			} else if (getArgs().get(0).equals("sell")) {
				if (transactionItem == null) {
					return new ServerOutputObject("You aren't carrying that.",
							Color.WHITE, 1);
				} else {
					return new ServerOutputObject("You sell "
							+ transactionItem.getShort() + " for "
							+ transactionPrice + " credits.", Color.WHITE, 1);
				}
			} else {
				return new ServerOutputObject(
						"shop list\nshop buy <item>\nshop sell <item>",
						Color.WHITE, 1);
			}
		} else {
			return null;
		}
	}

	private ServerOutputObject outputRoomBuy() {
		return new ServerOutputObject(Game.findPlayer(charName).getShort()
				+ " buys " + transactionItem.getShort() + ".", Color.WHITE, 1);
	}

	private ServerOutputObject outputRoomSell() {
		return new ServerOutputObject(Game.findPlayer(charName).getShort()
				+ " sells " + transactionItem.getShort() + ".", Color.WHITE, 1);
	}

	private Character findShop(String charName) {
		for (model.Character c : Game.localCharacters(charName)) {
			if (c instanceof ShopkeepMob) {
				return c;
			}
		}
		return null;
	}

	private int findPrice(Item i, boolean playerSelling) {
		Double price = (double) i.getValue();
		if (playerSelling)
			price = (shop.getCurrentPurchasePrice() * price);
		else
			price = (shop.getCurrentSalePrice() * price);
		return price.intValue();
	}

	private String shopList() {
		String output = shop.getShort() + " is selling:\n";
		for (Item i : shop.getInventory()) {
			output += findPrice(i, false) + "    " + i.getShort() + "\n";
		}
		return output;
	}
	
	private boolean canBuy() {
		return Game.findPlayer(charName).getCharStatus().getWorth() - findPrice(transactionItem, false) >= 0;
	}
}
