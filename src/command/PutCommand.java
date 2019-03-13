package command;

import java.awt.Color;

import model.Game;
import model.Item;
import items.ContainerItem;
import controller.Server;
import controller.ServerOutputObject;

public class PutCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3622191410077889227L;
	private Item putItem;
	private Item container;

	public PutCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() == 2) {
			putItem = Game.inPlayerInventory(charName, getArgs().get(0));
			container = Game.inPlayerInventory(charName, getArgs().get(1));
			if (putItem != null && container != null
					&& container instanceof ContainerItem) {
				putItem();
				server.sendToRoom(charName, outputRoom());
			}
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() < 1) {
			return new ServerOutputObject("Put what where?", Color.WHITE, 1);
		} else if (getArgs().size() == 1) {
			return new ServerOutputObject("Put it into what?", Color.WHITE, 1);
		} else if (getArgs().size() == 2) {
			if (container == null || putItem == null) {
				return new ServerOutputObject("You aren't carrying that.",
						Color.WHITE, 1);
			} else {
				return new ServerOutputObject("You put " + putItem.getShort()
						+ " into " + container.getShort() + ".", Color.WHITE, 1);
			}
		} else {
			return null;
		}
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " puts " + putItem.getShort()
				+ " into " + container.getShort() + ".", Color.white, 1);
	}

	private void putItem() {
		((ContainerItem) container).getInventory().add(putItem);
		Game.getPlayerInventory(charName).remove(putItem);
	}
}
