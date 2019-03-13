package command;

import java.awt.Color;

import model.Game;

import controller.Server;
import controller.ServerOutputObject;

public class ShutdownCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2926303627017641305L;
	private final String SHUTDOWN_PASSWORD = "12345";
	private boolean shutItDown = false;

	public ShutdownCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
		if (shutItDown) {
			server.sendToGlobal(charName, outputGlobal());
			server.shutdown();
		}
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() != 1) {
			return new ServerOutputObject("Syntax: shutdown <password>",
					Color.CYAN, 1);
		} else {
			if (getArgs().get(0).equals(SHUTDOWN_PASSWORD)) {
				shutItDown = true;
				return new ServerOutputObject(
						"1, 2, 3, 4, 5? \nAmazing, I have the same combination on my luggage!",
						Color.CYAN, 1);
			} else {
				return new ServerOutputObject(
						"Ah ah ah, you didn't say the magic word.", Color.CYAN,
						1);
			}
		}
	}

	private ServerOutputObject outputGlobal() {
		return new ServerOutputObject(
				"Time slows to a halt.\nThe world begins unmaking itself, leaving only void.",
				Color.DARK_GRAY, 1);
	}
}
