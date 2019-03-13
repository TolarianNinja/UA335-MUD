package model;

/**
 * @author Clint Parker
 *         Responsibilities
 *         -establish connections between an arbitrary amount of characters
 *         -sets up accounts, authenticates accounts
 *         -initializes games
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controller.AccountCollection;

@Deprecated
public class Server {

	private ServerSocket serverSocket;
	private AccountCollection ac;

	private class ClientAccepter implements Runnable {
		public void run() {
			while (true) {
				try {
					Socket s = serverSocket.accept(); // wait for a new client

					// grab the output and input streams for the new client
					ObjectOutputStream output = new ObjectOutputStream(
							s.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(
							s.getInputStream());

					String username = null, password, selection, tmp;
					boolean isValidUsrname = false, isValidPassword = false;

					// write the Welcome message when user connects
					output.writeObject("Welcome to MUD v0.1!\nPlease select from the following\n\t1) " +
							"Login to an existing account \n\t2) Create an account \n");

					do {
						// read the client's selection
						selection = (String) input.readObject();

						if (!(selection.equals("1") || selection.equals("2"))) {
							output.writeObject("Server error: selection `"
									+ selection + "' is not recognized\n");
						}
					} while (!(selection.equals("1") || selection.equals("2")));

					// if the client enters 1 to login, collect their login
					// information
					if (selection.equals("1")) {
						output.writeObject(drawLine() + "\n\t---Login---\n");
						output.writeObject("Enter username: ");

						do {

							tmp = (String) input.readObject();
							isValidUsrname = ac.isValidUsername(tmp);

							if (!isValidUsrname) {
								output.writeObject("Server error: username not found.\nEnter username: ");
							}

							else {
								username = tmp;
								output.writeObject("Valid username. Enter password: ");

								do {
									String in = (String) input.readObject();
									isValidPassword = ac.isValidPassword(
											username, in);

									if (!isValidPassword) {
										output.writeObject("\nServer error: username/password combination incorrect\nEnter password: ");
									} else {
										password = in;
										output.writeObject("\nPassword correct, loading account...\n"
												+ drawLine());
										break;
									}
								} while (true);

							}

							if (isValidPassword && isValidUsrname)
								break;
						} while (true);

					} else {
						// otherwise, the option was to create an account TODO
						output.writeObject(drawLine() + "\n\t---Account Creation---\n");
						output.writeObject("To create an account, enter a username: ");
						
						boolean isNameTaken = false;
						do {
							username = (String) input.readObject();
							isNameTaken = ac.isValidUsername(username);
							
							if (isNameTaken)
								output.writeObject("\nServer response: Error, username `" + username + "' already taken\nEnter a username: ");
							
						} while (isNameTaken);
						
						output.writeObject("\nEnter a password (must be > 4 characters): ");
						do {
							
							password = (String) input.readObject();
							
							if (password.length() < 5)
								output.writeObject("\nServer response: password length needs to be at least 5 characters." +
										"\nEnter password: ");
							
						} while (password.length() < 5);
						
					}

					// TODO: setup client in a new thread after they either
					// login or create a new account
					// when a user creates an account, they must specify an
					// absolute path to the save game file
					// this will be where game data will be loaded when they
					// login

					// outputs.put(name, output);
					// inputs.put(name, input);

					// create a command history queue for the new client
					// histories.put(name, new
					// LinkedBlockingDeque<Command<NetpaintServer>>());

					// start a new ClientHandler for this new client
					// new Thread(new ClientHandler(name,
					// histories.get(name))).start();
				} catch (Exception e) {
					System.err.println("In Client Accepter:");
					e.printStackTrace();
					break;
				}
			}
		}
	}

	private String drawLine() {
		String s = "";
		for (int i = 0; i < 35; i++) {
			s += "-";
		}
		return s;
	}

	/**
	 * @param port
	 *            to use in creating a socket
	 */
	public Server(int port) {
		try {
			ac = new AccountCollection();
			serverSocket = new ServerSocket(port);
			new Thread(new ClientAccepter()).start();
		} catch (Exception e) {
			System.err.println("Error creating server:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("listening on port 4200");
		new Server(4200);
	}

}
