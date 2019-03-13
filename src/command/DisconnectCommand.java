package command;

import java.awt.Color;

import model.Game;
import controller.Server;
import controller.ServerOutputObject;

public class DisconnectCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5114538664401523939L;

	public DisconnectCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToOthers(charName, outputOthers());
//		server.sendToRoom(charName, outputRoom());
		server.sendToUser(charName, outputUser());
//		Game.localPlayers(charName).remove(Game.findPlayer(charName));
//		Game.getAllPlayers().remove(Game.findPlayer(charName));
		server.disconnect(charName);
	}

	private ServerOutputObject outputUser() {
		return new ServerOutputObject(toUser(), Color.WHITE, 1);
	}

	private ServerOutputObject outputRoom() {
		return new ServerOutputObject(toRoom(), Color.WHITE, 1);
	}

	private ServerOutputObject outputOthers() {
		return new ServerOutputObject(toOthers(), Color.CYAN, 1, true);
	}

	public String toOthers() {
		return "\n=*= " + charName + " has left the world =*=";
	}

	public String toUser() {
		return "The world around you fades to black.";
	}

	public String toRoom() {
		return charName + " fades into the ether.";
	}
}
