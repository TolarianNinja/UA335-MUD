package command;

import java.awt.Color;

import model.Game;
import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;

public class TellCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4180464436604601186L;
	private String targetName;

	public TellCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 1) {
			targetName = Translator.upperFirst(getArgs().get(0));
			if (Game.findPlayer(targetName) != null) {
				server.sendToUser(targetName, outputTarget());
				server.prompt(targetName);
			}
		}
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject("Tell who what?", Color.WHITE, 1);
		} else if (getArgs().size() == 1) {
			return new ServerOutputObject("Tell them what?", Color.WHITE, 1);
		} else if (Game.findPlayer(targetName) == null) {
			return new ServerOutputObject("They aren't anywhere in the world.",
					Color.WHITE, 1);
		} else {
			return new ServerOutputObject("You tell " + targetName + " '"
					+ Translator.translateSpeech(charName, getTargetText())
					+ "'", Color.GREEN, 1, true);
		}
	}

	private ServerOutputObject outputTarget() {
		return new ServerOutputObject("\n" + charName + " tells you '"
				+ Translator.translateSpeech(charName, getTargetText()) + "'",
				Color.GREEN, 1, true);
	}
}
