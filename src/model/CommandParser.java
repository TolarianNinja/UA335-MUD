package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@Deprecated
public class CommandParser {
	private ArrayList<String> allInput;
	private HashMap<String, Command> commands;

	public CommandParser() {
		allInput = new ArrayList<String>();
		commands = new HashMap<String, Command>();
		SetUpCommands();
	}

	/**
	 * All of the commands that the MUD will allow to be used. Current list is
	 * everything needed in iteration 1.
	 */
	public void SetUpCommands() {
		// interacting with items
		commands.put("get", null);
		commands.put("drop", null);
		commands.put("inventory", null);

		// interacting with rooms
		commands.put("look", null);
		commands.put("north", null);
		commands.put("east", null);
		commands.put("south", null);
		commands.put("west", null);
		commands.put("up", null);
		commands.put("down", null);

		// social commands
		commands.put("who", null);
		commands.put("ooc", null);

		// other
		commands.put("score", null);
		commands.put("quit", null);
		commands.put("shutdown", null);

	}

	public void Parse(String input) {
		Scanner scan = new Scanner(input);
		allInput.clear();
		while (scan.hasNext()) {
			allInput.add(scan.next());
		}
		scan.close();
	}

	public String getCommand() {
		return allInput.get(0);
	}

	public ArrayList<String> getAllInput() {
		return allInput;
	}

	public int numArgs() {
		return allInput.size() - 1;
	}
}
