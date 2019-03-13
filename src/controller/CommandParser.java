package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import model.Direction;
import command.Command;
import command.AttackCommand;
import command.CancelCommand;
import command.CommandCommand;
import command.DestroyCommand;
import command.DisconnectCommand;
import command.DrinkCommand;
import command.DropCommand;
import command.EatCommand;
import command.EnterCommand;
import command.GetCommand;
import command.HackCommand;
import command.InventoryCommand;
import command.LookCommand;
import command.MoveCommand;
import command.OOCCommand;
import command.PutCommand;
import command.RemoveCommand;
import command.RestoreCommand;
import command.SayCommand;
import command.ScoreCommand;
import command.ShopCommand;
import command.ShutdownCommand;
import command.TellCommand;
import command.TempDamageCommand;
import command.WearCommand;
import command.WhoCommand;
import command.WorthCommand;

public class CommandParser {
	private ArrayList<String> allInput;
	private HashMap<String, Command<Server>> commands;
	private ArrayList<String> args;
	private String inputComm;
	private String charName;

	public CommandParser(String charName) {
		allInput = new ArrayList<String>();
		args = new ArrayList<String>();
		commands = new HashMap<String, Command<Server>>();
		inputComm = "";
		this.charName = charName;
		SetUpCommands();
	}

	/**
	 * All of the commands that the MUD will allow to be used. Current list is
	 * everything needed in iteration 1.
	 */
	public void SetUpCommands() {
		// interacting with items
		commands.put("get", new GetCommand(charName));
		commands.put("put", new PutCommand(charName));
		commands.put("drop", new DropCommand(charName));
		commands.put("inventory", new InventoryCommand(charName));
		commands.put("drink", new DrinkCommand(charName));
		commands.put("wear", new WearCommand(charName));
		commands.put("hold", new WearCommand(charName));
		commands.put("wield", new WearCommand(charName));
		commands.put("remove", new RemoveCommand(charName));
		commands.put("enter", new EnterCommand(charName));
		commands.put("destroy", new DestroyCommand(charName));
		commands.put("eat", new EatCommand(charName));
		commands.put("hack", new HackCommand(charName));

		// interacting with rooms
		commands.put("look", new LookCommand(charName));
		commands.put("north", new MoveCommand(Direction.NORTH, charName));
		commands.put("east", new MoveCommand(Direction.EAST, charName));
		commands.put("south", new MoveCommand(Direction.SOUTH, charName));
		commands.put("west", new MoveCommand(Direction.WEST, charName));
		commands.put("up", new MoveCommand(Direction.UP, charName));
		commands.put("down", new MoveCommand(Direction.DOWN, charName));
		
		// interacting with mobs
		commands.put("shop", new ShopCommand(charName));

		// social commands
		commands.put("who", new WhoCommand(charName));
		commands.put("ooc", new OOCCommand(charName));
		commands.put("say", new SayCommand(charName));
		commands.put("tell", new TellCommand(charName));

		// other
		commands.put("score", new ScoreCommand(charName));
		commands.put("commands", new CommandCommand(charName, allCommands()));
		commands.put("quit", new DisconnectCommand(charName));
		commands.put("shutdown", new ShutdownCommand(charName));
		commands.put( "attack", new AttackCommand( charName ) );
		commands.put("worth", new WorthCommand(charName));
		
		
		/* TEMPORARY/ TEST */
		commands.put("hurtself", new TempDamageCommand(charName));
		commands.put("restore", new RestoreCommand(charName));
		commands.put("cancel", new CancelCommand(charName));

	}

	public Command<Server> parse(String input) {
		Scanner scan = new Scanner(input);
		Command<Server> output;
		allInput.clear();
		args.clear();
		while (scan.hasNext()) {
			allInput.add(scan.next());
			//allInput.add(scan.next().toLowerCase());
		}
		if (allInput.isEmpty()) {
			scan.close();
			return null;
		}
		inputComm = allInput.get(0);
		for (int i = 1; i < allInput.size(); i++) {
			args.add(allInput.get(i));
		}

		output = commands.get(inputComm);

		if (output != null) {
			if (!args.isEmpty()) {
				output.setArguments(args);
			}
		}

		scan.close();
		return output;
	}

	public ArrayList<String> allCommands() {
		ArrayList<String> output = new ArrayList<String>();
		for (String comm : commands.keySet())
			if (commands.get(comm) != null)
				output.add(comm);
		return output;
	}

	public String getCommand() {
		return inputComm;
	}

	public ArrayList<String> getAllInput() {
		return allInput;
	}

	public int numArgs() {
		return args.size();
	}

	public ArrayList<String> getArgs() {
		return args;
	}
}
