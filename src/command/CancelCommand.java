package command;

import java.awt.Color;
import java.util.ArrayList;

import model.Game;
import model.Player;

import controller.Server;
import controller.ServerOutputObject;
import effects.Effect;

public class CancelCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8687794522681653530L;

	public CancelCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
		clearEffects(server, charName);
	}
	
	public ServerOutputObject outputUser() {
		return new ServerOutputObject("Your body purges all unnatural effects.", Color.WHITE, 1);
		
	}
	
	private void clearEffects(Server server, String charName) {
		Player p = Game.findPlayer(charName);
		ArrayList<Effect> removed = p.getCharStatus().getEffects();
		for (Effect e : removed) {
			server.sendToUser(charName, e.outputUserEnd());
		}
		p.getCharStatus().getEffects().clear();
	}
	

}
