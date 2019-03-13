package command;

import java.awt.Color;

import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class RemoveCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6863594808428904872L;
	private Item removedItem;

	public RemoveCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			removedItem = Game.inPlayerEquipment(charName, getArgs().get(0));
		}
		if (removedItem != null) {
			removeItem();
			server.sendToRoom(charName, outputRoom());
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Remove what?", Color.WHITE, 1);
		} else if (removedItem == null) {
			return new ServerOutputObject("You aren't wearing that.",
					Color.WHITE, 1);
		} else {
			return new ServerOutputObject("You stop using "
					+ removedItem.getShort() + ".", Color.WHITE, 1);
		}
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " stops wearing "
				+ removedItem.getShort() + ".", Color.white, 1);
	}

	private void removeItem() {
		Game.findPlayer(charName).removeEquipment(getArgs().get(0));
	}
}
