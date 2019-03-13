package command;

import java.awt.Color;

import model.Game;
import model.Item;

import controller.Server;
import controller.ServerOutputObject;

public class DestroyCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1714996706290360261L;
	private Item destroyItem;

	public DestroyCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			destroyItem = Game.inPlayerInventory(charName, getArgs().get(0));
		}
		if (destroyItem != null) {
			server.sendToRoom(charName, outputRoom());
			destroyItem();
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Destroy what?", Color.WHITE, 1);
		} else {
			if (destroyItem != null) {
				return new ServerOutputObject("You break "
						+ destroyItem.getShort() + " into " + (destroyItem.getValue() / 4) + " credits.", Color.CYAN,
						1);
			} else {
				return new ServerOutputObject("You don't see that here.",
						Color.WHITE, 1);
			}
		}
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " breaks "
				+ destroyItem.getShort() + " down to ether.", Color.white, 1);
	}

	private void destroyItem() {
		Game.findPlayer(charName).getCharStatus().changeWorth((destroyItem.getValue() / 4));
		Game.getPlayerInventory(charName).remove(destroyItem);
	}
}
