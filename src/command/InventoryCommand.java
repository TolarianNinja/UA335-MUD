package command;


import java.awt.Color;

import model.Game;
import model.Item;

import controller.Server;
import controller.ServerOutputObject;

public class InventoryCommand extends Command<Server> {

	private static final long serialVersionUID = -890351750348848859L;

	public InventoryCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		server.sendToUser(charName, outputUser());
	}

	private ServerOutputObject outputUser() {
		String output = "You are carrying:";
		for (Item i : Game.getPlayerInventory(charName)) {
			output += "\n   " + i.getShort();
		}
		return new ServerOutputObject(output, Color.WHITE, 1);
	}
}
