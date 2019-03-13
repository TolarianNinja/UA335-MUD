package command;

import java.awt.Color;

import model.Direction;
import model.Game;
import controller.Server;
import controller.ServerOutputObject;

public class MoveCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7808879738778818677L;
	private static final int moveCost = 3;
	private Direction direction;
	
	public MoveCommand(Direction direction, String charName) {
		super(charName);
		this.direction = direction;
	}

	@Override
	public void execute(Server server) {
		Integer exit = Game.getPlayerRoom(charName).getExits().get(direction);
		if (exit == null) {
			server.sendToUser(charName, outputUser());
		} else if (Game.findPlayer(charName).getCharStatus().getStamina() < moveCost) {
			server.sendToUser(charName, outputStamina());
		} else {
			server.sendToRoom(charName, outputRoomLeaving());
			movePlayer(charName, exit);
			Game.findPlayer(charName).getCharStatus().alterStamina(-moveCost);
			server.sendToRoom(charName, outputRoomEntering());
			new LookCommand(charName).execute(server);
		}
		
	}
	
	private ServerOutputObject outputRoomEntering() {
		return new ServerOutputObject(charName + " has arrived.", Color.white,
				1);
	}

	private ServerOutputObject outputRoomLeaving() {
		return new ServerOutputObject(charName + " leaves to the "
				+ direction.toString().toLowerCase() + ".", Color.white, 1);
	}

	private ServerOutputObject outputUser() {
		return new ServerOutputObject(
					"There isn't an exit in that direction.", Color.WHITE, 1);
	}
	
	private ServerOutputObject outputStamina() {
		return new ServerOutputObject("You are too tired to move.", Color.WHITE, 1);
	}
	
	public void movePlayer(String charName, int newRoom) {
		Game.findPlayer(charName).move(newRoom);
	}
}
