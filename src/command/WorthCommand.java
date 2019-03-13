package command;

import java.awt.Color;

import model.Game;

import controller.Server;
import controller.ServerOutputObject;

public class WorthCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2908043292467640541L;

	public WorthCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
	}
	
	public ServerOutputObject outputUser() {
		return new ServerOutputObject("Your account balance is " + charWorth() + " credits.", Color.WHITE, 1);
	}
	
	public int charWorth() {
		return Game.findPlayer(charName).getCharStatus().getWorth();
	}

}
