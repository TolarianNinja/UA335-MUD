package command;

import items.EdibleItem;

import java.awt.Color;

import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;
import effects.Effect;

public class EatCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2925593843600864862L;
	private Item itemAte;

	public EatCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		boolean isEaten = false;
		if (getArgs().size() > 0) {
			itemAte = Game.inPlayerInventory(charName, getArgs().get(0));
		} if (itemAte != null && itemAte instanceof EdibleItem) {
			server.sendToRoom(charName, outputRoom());
			isEaten = true;
		}
		server.sendToUser(charName, outputUser());
		if (isEaten) {
			eatItem();
			applyEffect(server);
		}
	}
	
	// remove the item from the inventory when it's eaten
	private void eatItem() {
		Game.getPlayerInventory(charName).remove(itemAte);
	}
	
	public ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Eat what?", Color.WHITE, 1);
		} else if (itemAte == null) {
			return new ServerOutputObject("You aren't carrying that.", Color.WHITE, 1);
		} else if (!(itemAte instanceof EdibleItem)) {
			return new ServerOutputObject("You can't eat that.", Color.WHITE, 1);
		} else {
			return new ServerOutputObject("You eat " + itemAte.getShort() + ".", Color.WHITE, 1);
		}
	}
	
	public ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " eats " + itemAte.getShort() + ".", Color.WHITE, 1);
		
	}
	
	// apply each effect on the item to the person
	protected void applyEffect(Server server) {
		for (Effect ef : itemAte.getEffects()) {
			ef.addEffect(charName);
			server.sendToUser(charName, ef.outputUserBegin());
		}
	}

}
