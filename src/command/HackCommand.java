package command;

import java.awt.Color;

import items.HackableItem;
import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class HackCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1812140051540841879L;
	Item hacked;
	boolean hackSuccess = false;

	public HackCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() == 1) {
			hacked = Game.findLocalItem(charName, getArgs().get(0));
			if (hacked != null) {
				if (hacked instanceof HackableItem && hacked.canHack() && !hacked.isHacked()) {
					hackSuccess = true;
					server.sendToRoom(charName, outputRoom());
				}
			}
		}
		server.sendToUser(charName, outputUser());
		if (hackSuccess) {
			hacked.use(charName);
			server.sendToUser(charName, hacked.hackString());
		}
	}

	public ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Hack what?", Color.WHITE, 1);
		} else if (hacked == null) {
			return new ServerOutputObject("You don't see that here.",
					Color.WHITE, 1);
		} else if (hacked.isHacked()) {
			return new ServerOutputObject("It looks like someone already hacked it.", Color.WHITE, 1);
		} else if (!(hacked instanceof HackableItem) || !hacked.canHack()) {
			return new ServerOutputObject("You can't hack that.", Color.WHITE,
					1);
		}
		return new ServerOutputObject("You hack " + hacked.getShort() + ".",
				Color.WHITE, 1);

	}

	public ServerOutputObject outputRoom() {
		return null;

	}
}
