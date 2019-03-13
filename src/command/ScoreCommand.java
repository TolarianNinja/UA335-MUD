package command;

import java.awt.Color;

import model.CharacterStatus;
import model.Game;
import controller.Server;
import controller.ServerOutputObject;

public class ScoreCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6280214001273432837L;

	public ScoreCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		return new ServerOutputObject(scoreOutput(), Color.WHITE, 1);
	}

	private String scoreOutput() {
		CharacterStatus status = Game.findPlayer(charName).getCharStatus();
//		int level = Game.findPlayer(charName).getCharFeat().calculateLevel();

		String output = "Name: " + charName + "  Level: " //+ level
				+ "\nHealth: " + status.getCurrentHealth() + " Energy: "
				+ status.getEnergy() + " Stamina: " + status.getStamina()
				+ "\nWorth: " + status.getWorth() + " credits";
		return output;
	}
}