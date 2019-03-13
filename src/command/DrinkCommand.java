package command;

import java.awt.Color;

import model.Game;
import model.Item;
import items.DrinkContainerItem;
import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;
import effects.Effect;

public class DrinkCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6192964811136244795L;
	private Item drink;

	public DrinkCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		boolean drank = false;
		if (getArgs().size() > 0) {
			drink = Game.inPlayerInventory(charName, getArgs().get(0));
			if (drink == null)
				drink = Game.inPlayerEquipment(charName, getArgs().get(0));
			if (drink instanceof DrinkContainerItem
					&& !((DrinkContainerItem) drink).isEmpty()) {
				server.sendToRoom(charName, outputRoom());
				drank = true;
			}
		}
		server.sendToUser(charName, outputUser());

		// done with boolean to send effect strings after the drink string
		if (drank) {
			drinkItem(server);
			applyEffect(server);
		}
	}

	// The output that is sent to the player
	private ServerOutputObject outputUser() {
		if (getArgs().size() < 1) {
			return new ServerOutputObject("Drink from what?", Color.WHITE, 1);
		} else if (getArgs().size() == 1) {
			if (drink == null) {
				return new ServerOutputObject("You aren't carrying that.",
						Color.WHITE, 1);
			} else if (!(drink instanceof DrinkContainerItem)) {
				return new ServerOutputObject("You can't drink from that.",
						Color.WHITE, 1);
			} else {
				if (((DrinkContainerItem) drink).isEmpty()) {
					return new ServerOutputObject(Translator.upperFirst(drink.getShort())
							+ " is completely empty!", Color.WHITE, 1);
				} else {
					return new ServerOutputObject("You drink "
							+ ((DrinkContainerItem) drink).getLiquidColor()
							+ " liquid from " + drink.getShort() + ".",
							Color.WHITE, 1);
				}
			}
		} else {
			return null;
		}
	}

	// The output that is sent to the room
	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " drinks "
				+ ((DrinkContainerItem) drink).getLiquidColor()
				+ " liquid from " + drink.getShort() + ".", Color.white, 1);
	}

	// Activate the item if it is the right type
	private void drinkItem(Server server) {
		if (drink instanceof DrinkContainerItem) {
			((DrinkContainerItem) drink).use(charName);
		}
	}

	// If there are effects on the drink item, apply them to the person who
	// drank
	protected void applyEffect(Server server) {
		for (Effect ef : drink.getEffects()) {
			ef.addEffect(charName);
			server.sendToUser(charName, ef.outputUserBegin());
		}
	}
}