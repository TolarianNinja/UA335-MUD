package command;

import items.ContainerItem;

import java.awt.Color;

import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class GetCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6454583809106552662L;
	Item grabbedItem; // The item desired
	ContainerItem container; // The container the item is in (when containers
								// are

	// implemented)

	public GetCommand(String charName) {
		super(charName);

	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 1) {
			container = (ContainerItem) Game.findLocalItem(charName,
					getArgs().get(1));
		}
		if (getArgs().size() > 0) {
			grabbedItem = Game.findRoomItem(charName, getArgs().get(0));
			if (grabbedItem == null && container != null) {
				for (Item i : container.getInventory()) {
					if (i.getName().contains(getArgs().get(0))) {
						grabbedItem = i;
						getFromContainer();
						server.sendToRoom(charName, outputRoom());
						break;
					}
				}
			} else if (grabbedItem != null && container == null
					&& grabbedItem.canPickUp()) {
				getFromRoom();
				server.sendToRoom(charName, outputRoom());
			}
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Get what?", Color.white, 1);
		} else if (getArgs().size() == 1) {
			if (grabbedItem == null) {
				return new ServerOutputObject("You don't see that here.",
						Color.white, 1);
			} else if (!grabbedItem.canPickUp()) {
				return new ServerOutputObject("You can't pick that up.",
						Color.WHITE, 1);
			} else {
				return new ServerOutputObject("You get "
						+ grabbedItem.getShort() + ".", Color.white, 1);
			}
		} else if (getArgs().size() == 2) {
			if (grabbedItem != null && container != null) {
				return new ServerOutputObject("You get "
						+ grabbedItem.getShort() + " from "
						+ container.getShort() + ".", Color.WHITE, 1);
			} else if (grabbedItem == null && container != null) {
				return new ServerOutputObject("You don't see that in "
						+ container.getShort() + ".", Color.WHITE, 1);
			} else {
				return new ServerOutputObject("You don't see that here.", Color.WHITE, 1);
			}
		} else {
			return null;
		}
	}

	private ServerOutputObject outputRoom() {
		if (container == null) {
			return new ServerOutputObject(charName + " gets "
					+ grabbedItem.getShort() + ".", Color.white, 1);
		} else {
			return new ServerOutputObject(charName + " gets "
					+ grabbedItem.getShort() + " from " + container.getShort()
					+ ".", Color.WHITE, 1);
		}
	}

	private void getFromRoom() {
		Game.getPlayerInventory(charName).add(grabbedItem);
		Game.getPlayerRoom(charName).getItems().remove(grabbedItem);
	}

	private void getFromContainer() {
		Game.getPlayerInventory(charName).add(grabbedItem);
		container.getInventory().remove(grabbedItem);
	}
}
