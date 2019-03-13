package command;

import java.awt.Color;

import model.Game;
import model.Player;

import controller.Server;
import controller.ServerOutputObject;

public class WhoCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5145728996202452536L;

	public WhoCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		String output = "Players currently online:";
		for (Player p : Game.getAllPlayers()) {
			output += "\n   " + p.getPlayerName();
		} output += "\n";
		return new ServerOutputObject(output, Color.WHITE, 1);
	}
}
