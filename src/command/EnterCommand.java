package command;

import items.PortalItem;

import java.awt.Color;

import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class EnterCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7560019515913709987L;
	private Item portal;

	public EnterCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			portal = Game.findLocalItem(charName, getArgs().get(0));
		}
		if (portal != null && portal instanceof PortalItem) {
			server.sendToRoom(charName, outputRoom());
			((PortalItem) portal).use(charName);
			server.sendToUser(charName, outputUser());
			new LookCommand(charName).execute(server);
		} else {
			server.sendToUser(charName, outputUser());
		}
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Enter what?", Color.WHITE, 1);
		} else if (getArgs().size() > 0) {
			if (portal != null) {
				if (portal instanceof PortalItem) {
					return new ServerOutputObject("You enter "
							+ portal.getShort() + ".", Color.WHITE, 1);
				} else {
					return new ServerOutputObject("You can't enter that.",
							Color.WHITE, 1);
				}
			} else {
				return new ServerOutputObject("You don't see that here.",
						Color.WHITE, 1);
			}
		} else {
			return null;
		}
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(charName + " enters " + portal.getShort()
				+ ".", Color.white, 1);
	}
}
