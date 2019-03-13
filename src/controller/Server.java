package controller;

/**
 * @author Clint Parker
 *         Responsibilities
 *         -establish connections between an arbitrary amount of characters
 *         -sets up accounts, authenticates accounts
 *         -initializes games
 */

import java.awt.Color;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import model.Account;
import model.Character;
import model.CharacterStatus;
import model.Game;
import model.Player;
import model.Room;
import command.Command;
import command.DisconnectCommand;
import command.LookCommand;

public class Server {

	private ServerSocket serverSocket;
	private AccountCollection ac;
	private Map<String, ObjectOutputStream> outputs;
	private Map<String, ObjectInputStream> inputs;

	/**
	 * Class that listens for commands from the Client
	 */
	private class CharacterHandler implements Runnable {
		private ObjectInputStream input;
		private CommandParser parser;
		private String charName;

		public CharacterHandler(ObjectInputStream input, String characterName) {
			this.input = input;
			this.parser = new CommandParser(characterName);
			this.charName = characterName;
			Game.LoginPlayer(charName, ac);
			new LookCommand(charName).execute(Server.this);
			prompt(charName);
			sendToOthers(charName, userLogin());
		}
		
		private ServerOutputObject userLogin() {
			return new ServerOutputObject("=*=  " + charName + " has entered the world  =*=", Color.CYAN, 1);
		}

		@Override
		public void run() {
			try {
				while (true) {
					// read a command from the client, execute on the server
					String readString = null;
					try {
						readString = (String) input.readObject();
					} catch (Exception e) {
					}

					if (readString != null) {

						Command<Server> command = (Command<Server>) parser
								.parse(readString);
						if (command != null)
							command.execute(Server.this);
						else
							badInput(charName);

						// terminate if client is disconnecting
						if (command instanceof DisconnectCommand) {
							input.close();
							return;
						}
						prompt(charName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ConnectionReceiver acts as a program thread dedicated to detecting a
	 * connection from outside. When an incoming "signal" is detected and a
	 * connection is established, that Socket's client will then have its own
	 * data stream path. Finally, a new threaded program--dedicated to getting a
	 * client in-game--is instanced that takes in those data streams. This is so
	 * that people can sign on and join the game at the same time.
	 * 
	 * @author Christian Greyeyes
	 * 
	 */

	private class ConnectionReceiver implements Runnable {

		public ConnectionReceiver() {
		}

		@Override
		public void run() {
			while (true) {
				try {
					Socket incoming = serverSocket.accept();

					// grab the output and input streams for the new client
					ObjectOutputStream output = new ObjectOutputStream(
							incoming.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(
							incoming.getInputStream());

					System.out.println("incoming:   " + incoming.toString());

					Thread gameJoin = new Thread(new CharacterAccepter(input,
							output, incoming));
					gameJoin.start();

				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}

		}

	}

	/**
	 * Class that will either login or create a user.
	 */
	private class CharacterAccepter implements Runnable {

		private ObjectInputStream input;
		private ObjectOutputStream output;
		private Socket sock;

		public CharacterAccepter(ObjectInputStream input,
				ObjectOutputStream output, Socket s) {
			this.input = input;
			this.output = output;
			sock = s;
		}

		public void run() {
			try {
				// Socket s = serverSocket.accept(); // wait for a new client

				String characterName = null, password, selection, tmp;
				boolean isValidUsrname = false, isValidPassword = false;

				output.writeObject(new ServerOutputObject(
						"Welcome to MUD v0.1!", Color.GREEN, 1));
				output.writeObject(new ServerOutputObject(
						"Please select from the following", Color.GREEN, 1));
				output.writeObject(new ServerOutputObject(
						"\t1) Login to an existing account", Color.ORANGE, 1));
				output.writeObject(new ServerOutputObject(
						"\t2) Create an account", Color.ORANGE, 1));

				do {
					// read the client's selection
					selection = (String) input.readObject();

					if (!(selection.equals("1") || selection.equals("2"))) {
						output.writeObject(new ServerOutputObject(
								"Server error: selection `" + selection
										+ "' is not recognized\n", Color.RED, 2));
					}
				} while (!(selection.equals("1") || selection.equals("2")));

				// if the client enters 1 to login, collect their login
				// information
				if (selection.equals("1")) {
					output.writeObject(new ServerOutputObject(drawLine()
							+ "\n\t---Login---\n", Color.CYAN, 1));
					output.writeObject(new ServerOutputObject(
							"Enter username: ", Color.CYAN, 1));

					do {
						try {
						tmp = (String) input.readObject();
						} catch (EOFException e) {
							continue;
						}
						isValidUsrname = ac.isValidUsername(tmp);

						if (!isValidUsrname) {
							output.writeObject(new ServerOutputObject(
									"Server error: username not found.\nEnter username: ",
									Color.RED, 1));
						}

						else {
							characterName = tmp;
							/*
							 * If the user is already online, close the
							 * connection
							 */
							if (ac.findAccount(characterName).isOnline()) {
								output.writeObject(new ServerOutputObject(
										"Server: (error) user already online",
										Color.RED, 1));
								disconnect(characterName);
								sock.close();
								continue;
							}
							output.writeObject(new ServerOutputObject(
									"Valid username, <" + characterName
											+ "> Now, enter password: ",
									Color.GREEN, 2));

							do {
								String in = (String) input.readObject();
								isValidPassword = ac.isValidPassword(
										characterName, in);

								if (!isValidPassword) {
									output.writeObject(new ServerOutputObject(
											"Server error: username/password combination incorrect",
											Color.RED, 1));
									output.writeObject(new ServerOutputObject(
											"Enter password: ", Color.GREEN, 1));
								} else {
									password = in;
									output.writeObject(new ServerOutputObject(
											"Password correct, loading account..."
													+ drawLine(), Color.GREEN,
											1));
									break;
								}
							} while (true);

						}

						if (isValidPassword && isValidUsrname)
							break;
					} while (true);

					// set up client in a new thread, map this character
					// name to their output stream
					outputs.put(characterName, output);
					inputs.put(characterName, input);

					/*
					 * after the user entered the correct credentials, set their
					 * status to online
					 */
					Account thisCharacter = ac.findAccount(characterName);
					thisCharacter.setOnline();

					new Thread(new CharacterHandler(input, characterName))
							.start();

				} else {

					output.writeObject(new ServerOutputObject(drawLine()
							+ "\n\t---Account Creation---\n", Color.blue, 1));
					output.writeObject(new ServerOutputObject(
							"To create an account, enter a character name: ",
							Color.blue, 1));

					boolean isNameTaken = false;
					do {
						characterName = (String) input.readObject();
						isNameTaken = ac.isValidUsername(characterName);

						if (isNameTaken)
							output.writeObject(new ServerOutputObject(
									"\nServer response: Error, character name `"
											+ characterName
											+ "' already taken\nEnter a character name: , color, newLines)",
									Color.RED, 1));

					} while (isNameTaken);

					output.writeObject(new ServerOutputObject(
							"\nEnter a password (must be > 4 characters): ",
							Color.blue, 1));
					do {

						password = (String) input.readObject();

						if (password.length() < 5)
							output.writeObject(new ServerOutputObject(
									"\nServer response: password length needs to be at least 5 characters."
											+ "\nEnter password: ", Color.red,
									1));

					} while (password.length() < 5);

					ac.add(new Account(characterName, password));

					output.writeObject(new ServerOutputObject(
							"Thank you, loading world...", Color.green, 1));

					outputs.put(characterName, output);
					new Thread(new CharacterHandler(input, characterName))
							.start();

				}

			} catch (Exception e) {
				System.err.println("In Client Accepter:");
				e.printStackTrace();
				// break; // since this class executes in threads, a thread
				// simply terminates when something goes wrong
			}
		}
	}

	/**
	 * @return a "line" from '-" characters of length 35
	 */
	private String drawLine() {
		String s = "";
		for (int i = 0; i < 35; i++) {
			s += "-";
		}
		return s;
	}

	public void badInput(String charName) {
		sendToStream(charName, "Check 'commands' for a list of commands.",
				Color.white, 1);
	}

	/**
	 * @param port
	 *            to use in creating a socket
	 */
	public Server(int port) {

		try {
			ac = new AccountCollection();
			serverSocket = new ServerSocket(port);
			outputs = new ConcurrentHashMap<String, ObjectOutputStream>();
			inputs = new ConcurrentHashMap<String, ObjectInputStream>();
			new Game();
			new Thread(new ConnectionReceiver()).start();
		} catch (Exception e) {
			System.err.println("Error creating server:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("listening on port 4200");
		new Server(4200);
	}

	/**
	 * Disconnects a given user
	 * 
	 * @param clientName
	 *            user to disconnect
	 */
	public void disconnect(String characterName) {
		try {
			//inputs.get(characterName).close();
			//inputs.remove(characterName);
			outputs.get(characterName).close(); // close output stream
			outputs.remove(characterName); // remove from map

			System.out.println(Game.findPlayer(characterName).getPlayerName());
			ac.quitPlayer(characterName);
			
			// remove from local room that the player is in--no longer going to be there
			Room r = Game.getPlayerRoom(characterName);
			ArrayList<Character> charList = r.getCharacters();
			for (int i = 0; i < charList.size(); i++) {
				if (charList.get(i).getName().contains(characterName)) {
					charList.remove(i);
					break;
				}
			}
			
//			ac.savePlayer(Game.findPlayer(characterName));
			// Game.getAllPlayers().remove(characterName);
			Game.localPlayers(characterName).remove(Game.findPlayer(characterName));
			Game.getAllPlayers().remove(Game.findPlayer(characterName));
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ObjectOutputStream getOutput(String charName) {
		return outputs.get(charName);
	}

	public Map<String, ObjectOutputStream> getStreams() {
		return outputs;
	}

	/**
	 * send the same string to all other users in the same room as charName
	 * 
	 * @param charName
	 *            the username to send all players in the same room
	 * @param output
	 *            pre-built output object to send to stream
	 */
	public void sendToRoom(String charName, ServerOutputObject output) {
		for (Player p : Game.localPlayers(charName)) {
			if (!p.getPlayerName().equals(charName)) {
				sendToStream(p.getPlayerName(), new ServerOutputObject("",
						Color.WHITE, 1));
				sendToStream(p.getPlayerName(), output);
				prompt(p.getPlayerName());
			}
		}
	}

	/**
	 * send the string to charName
	 * 
	 * @param charName
	 *            the username to send all players in the same room
	 * @param output
	 *            pre-built output object to send to stream
	 */
	public void sendToUser(String charName, ServerOutputObject output) {
		sendToStream(charName, output);
	}

	/**
	 * send the same string to all users logged in other than charName.
	 * 
	 * @param charName
	 *            the username to send all players in the same room
	 * @param output
	 *            pre-built output object to send to stream
	 */
	public void sendToOthers(String charName, ServerOutputObject output) {
		for (Player p : Game.getAllPlayers()) {
			if (!p.getPlayerName().equals(charName)) {
				sendToStream(p.getPlayerName(), new ServerOutputObject("",
						Color.WHITE, 1));
				sendToStream(p.getPlayerName(), output);
				prompt(p.getPlayerName());
			}
		}
	}

	/**
	 * send the same string to all users logged in.
	 * 
	 * @param charName
	 *            the username to send all players in the same room
	 * @param output
	 *            pre-built output object to send to stream
	 */
	public void sendToGlobal(String charName, ServerOutputObject output) {
		for (String stream : getStreams().keySet()) {
			sendToStream(stream, output);
		}
	}

	/**
	 * send strings to console using pre-built objects
	 * 
	 * @param charName
	 *            name of stream to send to
	 * @param output
	 *            pre-built ServerOutputObject, used to clean up code to send
	 *            single color strings
	 */
	public synchronized void sendToStream(String charName,
			ServerOutputObject output) {
		try {
			if (getOutput(charName) != null && output != null) {
				getOutput(charName).writeObject(output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send strings to the console
	 * 
	 * @param charName
	 *            name of stream to send to
	 * @param text
	 *            text to send to stream
	 * @param color
	 *            color of the string
	 * @param newLines
	 *            number of new lines after it (1 is standard, 0 if
	 *            multi-colored line)
	 */
	public synchronized void sendToStream(String charName, String text,
			Color color, int newLines) {
		try {
			if (getOutput(charName) != null)
				getOutput(charName).writeObject(
						new ServerOutputObject(text, color, newLines));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send strings to the console, used if it's a chat string
	 * 
	 * @param charName
	 *            name of stream to send to
	 * @param text
	 *            text to send to stream
	 * @param color
	 *            color of the string
	 * @param newLines
	 *            number of new lines after it (1 is standard, 0 if
	 *            multi-colored line)
	 * @param isChat
	 *            set true if it should be sent to chat window as well
	 */
	public synchronized void sendToStream(String charName, String text,
			Color color, int newLines, boolean isChat) {
		try {
			if (getOutput(charName) != null)
				getOutput(charName).writeObject(
						new ServerOutputObject(text, color, newLines, isChat));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param charName
	 *            Sends the prompt of charName to their stream
	 */
	public synchronized void prompt(String charName) {
		CharacterStatus stat = Game.findPlayer(charName).getCharStatus();
		
		int hp = stat.getCurrentHealth();
		int en = stat.getEnergy();
		int st = stat.getStamina();
		
		sendToStream(charName, "\n<" + hp + "hp " + en + "energy " + st
				+ "stamina> ", Color.LIGHT_GRAY, 0);
	}

	public void showAllPrompts() {
		for (String pName : outputs.keySet()) {
			prompt(pName);
		}
	}

	/**
	 * this method will set each character currently online, offline, and
	 * subsequently disconnect them.
	 */
	public synchronized void shutdown() {
		ArrayList<Account> onlineUsers = ac.getOnlineUsers();

		for (Account character : onlineUsers) {
			String name = character.getCharacterName();
			character.setOffline();
			disconnect(name); // disconnect will save the player as well
		}

		System.out.println("Server going down for halt!");
		// shutdown sever
		System.exit(0);
	}

}
