package command;

import java.awt.Color;
import java.util.ArrayList;

import controller.Server;

public class CommandCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3777401015695068468L;
	private ArrayList<String> comm;

	public CommandCommand(String charName, ArrayList<String> comm) {
		super(charName);
		this.comm = comm;
	}

	@Override
	public void execute(Server server) {
		server.sendToStream(charName, "Available commands:", Color.yellow, 1);
		server.sendToStream(charName, commOutput(), Color.white, 1);
	}
	
	public String commOutput() {
		String output = "";
		for (String s : comm) {
			output += s + " ";
		}
		return output;
	}

}
