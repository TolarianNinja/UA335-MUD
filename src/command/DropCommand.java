package command;


import java.awt.Color;

import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class DropCommand extends Command<Server> {

	private Item droppedItem;
	private static final long serialVersionUID = -5467289780651723868L;

	public DropCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			droppedItem = Game.inPlayerInventory(charName, getArgs().get(0));
		}
		if (droppedItem != null) {
			drop();
			server.sendToRoom(charName, outputRoom());
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Drop what?", Color.white, 1);
		} else if (getArgs().size() == 1) {
			if (droppedItem == null) {
				return new ServerOutputObject("You aren't carrying that.",
						Color.white, 1);
			} else {
				return new ServerOutputObject("You drop " + droppedItem.getShort() + ".",
						Color.white, 1);
			}
		} else {
			return null;
		}
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " drops "
						+ droppedItem.getShort() + ".", Color.white, 1);
	}

	private void drop() {
		Game.getPlayerInventory(charName).remove(droppedItem);
		Game.getPlayerRoom(charName).getItems().add(droppedItem);
	}
}
