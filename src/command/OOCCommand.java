package command;

import java.awt.Color;

import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;

public class OOCCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5873482421456861771L;

	public OOCCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0)
			server.sendToOthers(charName, outputOthers());
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Say what over OOC?", Color.WHITE, 1);
		} else {
			return new ServerOutputObject("[OOC] You say '"
					+ Translator.translateSpeech(charName, getText()) + "'",
					Color.yellow, 1, true);
		}
	}

	private ServerOutputObject outputOthers() {
		return new ServerOutputObject("[OOC] " + charName + " says '"
				+ Translator.translateSpeech(charName, getText()) + "'",
				Color.orange, 1, true);
	}
}
