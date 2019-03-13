package command;

import java.awt.Color;

import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;

public class SayCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -248457917492058826L;

	public SayCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0)
			server.sendToRoom(charName, roomOutput());
		server.sendToUser(charName, userOutput());
	}

	public ServerOutputObject userOutput() {
		if (getArgs().size() == 0)
			return new ServerOutputObject("Say whaaaaat?", Color.WHITE, 1);
		else
			return new ServerOutputObject("You say '"
					+ Translator.translateSpeech(charName, getText()) + "'",
					Color.CYAN, 1, true);
	}

	public ServerOutputObject roomOutput() {
		return new ServerOutputObject(charName + " says '"
				+ Translator.translateSpeech(charName, getText()) + "'",
				Color.CYAN, 1, true);
	}
}
