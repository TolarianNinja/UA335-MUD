package command;

import java.awt.Color;

import model.Game;
import model.Character;
import model.Player;
import controller.Server;
import controller.ServerOutputObject;

public class RestoreCommand extends Command<Server> {

	public RestoreCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			if (getArgs().get(0).equals("all")) {
				server.sendToGlobal(charName, output());
				restoreAll();
			} else if (Game.findPlayer(getArgs().get(0)) != null) {
				server.sendToUser(getArgs().get(0), output());
				restore(getArgs().get(0));
			} else {
				server.sendToUser(charName, noTarget());
			}
		} else {
			server.sendToRoom(charName, output());
			server.sendToUser(charName, output());
			restoreRoom(charName);
		}
	}

	private ServerOutputObject output() {
		return new ServerOutputObject(
				"A brilliant light flows through you, restoring your body.",
				Color.yellow, 1);
	}
	
	private ServerOutputObject noTarget() {
		return new ServerOutputObject("They are nowhere to be found in the world..", Color.WHITE, 1);
	}

	private void restore(String charName) {
		Game.findPlayer(charName).getCharStatus().regenerate();
	}

	private void restoreRoom(String charName) {
		for (Character c : Game.localCharacters(charName)) {
			c.getCharStatus().regenerate();
		}
	}

	private void restoreAll() {
		for (Player p : Game.getAllPlayers()) {
			p.getCharStatus().regenerate();
		}
	}

}
