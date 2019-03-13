package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import model.Account;
import model.Game;
import model.Player;

/**
 * 
 * @author Clint Parker Contains a collection of all accounts that have been
 *         made.
 */
public class AccountCollection {

	private List<Account> accounts;
	private ArrayList<Player> players;
	private static String accountsAbsPath = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "accounts.data";
	private static String playersAbsPath = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "players.data";
	private File accountsFile = new File(accountsAbsPath);
	private File playersFile = new File(playersAbsPath);

	public AccountCollection() throws ClassNotFoundException, IOException {
		// create this file, to contain all accounts, if it doesn't exist
		if (!accountsFile.exists()) {
			try {
				accountsFile.createNewFile();
			} catch (IOException e) {
				System.err.println("couldn't make file " + e);
			}
		}

		if (!playersFile.exists()) {
			try {
				playersFile.createNewFile();
			} catch (IOException e) {
				System.err.println("couldn't make file " + e);
			}
		}

		accounts = new ArrayList<Account>();
		players = new ArrayList<Player>();
		loadAccounts();
		loadAllPlayers();
	}

	/**
	 * Loads all accounts.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void loadAccounts() throws IOException, ClassNotFoundException {
		if (accountsFile.length() == 0)
			return;

		FileInputStream fis = new FileInputStream(accountsAbsPath);
		ObjectInputStream ins = new ObjectInputStream(fis);

		accounts = (List<Account>) ins.readObject();

		fis.close();
		ins.close();
	}

	/**
	 * Saves all accounts. This method should be called after the server creates
	 * a new account.
	 * 
	 * @throws IOException
	 */
	protected void saveAccounts() throws IOException {

		FileOutputStream fos = new FileOutputStream(accountsAbsPath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(accounts);

		oos.close();
		fos.close();

	}

	// this is just for initial testing for the server client login
	// sequence in the beginning of the game
	public void testInit() {
		accounts.add(new Account("Test1", "pass1"));
		accounts.add(new Account("Test2", "pass2"));
		accounts.add(new Account("Test3", "pass3"));
	}

	/**
	 * @param index
	 * @return Account at the specified index
	 */
	protected Account getAccountAt(int index) {
		if (index > accounts.size() || index < 0)
			return null;
		return accounts.get(index);
	}

	/**
	 * @param account
	 * @return true if account is an account
	 */
	protected boolean isAccount(Account account) {
		Iterator<Account> i = accounts.iterator();

		while (i.hasNext()) {
			if (i.next().equals(account))
				return true;
		}
		return false;
	}

	/**
	 * @return size of the collection
	 */
	protected int sizeOf() {
		return accounts.size();
	}

	protected void add(Account account) throws IOException {
		accounts.add(account);

		// save all accounts after one has been created and added
		saveAccounts();

	}

	/**
	 * persist the list of accounts after an exit (signoff) (if offline)
	 */
	// protected void persistAll() {
	// Iterator<Account> i = accounts.iterator();
	//
	//
	// }

	/**
	 * @param username
	 * @return true if user name is in list of accounts
	 */
	public boolean isValidUsername(String username) {
		Iterator<Account> i = accounts.iterator();

		while (i.hasNext()) {
			if (i.next().getCharacterName().equals(username))
				return true;
		}
		return false;
	}

	/**
	 * @return subset of accounts -- online accounts
	 */
	public ArrayList<Account> getOnlineUsers() {
		Iterator<Account> i = accounts.iterator();
		ArrayList<Account> onlineUsers = new ArrayList<Account>();

		while (i.hasNext()) {
			Account a = i.next();
			if (a.isOnline())
				onlineUsers.add(a);
		}
		return onlineUsers;
	}

	/**
	 * @param username
	 * @param pass
	 * @return true if associated user name and password are in the list of
	 *         account
	 */
	public boolean isValidPassword(String username, String pass) {
		Iterator<Account> i = accounts.iterator();

		while (i.hasNext()) {
			Account a = i.next();
			if (a.getCharacterName().equals(username)
					&& a.getPassword().equals(pass))
				return true;
		}
		return false;
	}

	/**
	 * @param characterName
	 *            to set online set @param online
	 */
	public void setOnline(String characterName) {
		Account a = findAccount(characterName);
		if (a != null)
			a.setOnline();
	}

	/**
	 * @param characterName
	 *            to find
	 * @return account if exists, null otherwise.
	 */
	public Account findAccount(String characterName) {
		Iterator<Account> i = accounts.iterator();

		while (i.hasNext()) {
			Account a = i.next();
			if (a.getCharacterName().equals(characterName))
				return a;
		}
		return null;
	}

	public void savePlayers() {

	}

	public void loadAllPlayers() throws IOException, ClassNotFoundException {
		if (playersFile.length() == 0)
			return;

		FileInputStream fis = new FileInputStream(playersAbsPath);
		ObjectInputStream ins = new ObjectInputStream(fis);

		players = (ArrayList<Player>) ins.readObject();

		fis.close();
		ins.close();
	}

	public void findPlayer() {

	}

	/**
	 * @param player
	 *            to save
	 * @throws IOException
	 */
	public void savePlayer(Player player) {
		// System.out.println(player.getCurrentRoomVnum() + ", " +
		// player.getName());
		// System.out.println(players.remove(player));
		//
		try {
			try {
				loadAllPlayers();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		remove(player);
		players.add(player);
		System.out.println("adding player " + player.getPlayerName());

		// HashMap<String, Player> m = Game.getAllPlayers();
		// Set<Entry<String, Player>> s = m.entrySet();
		// //
		// boolean found = false;
		// for (Entry<String, Player> entry : s) {
		// if (entry.getKey().equals(player.getPlayerName())) {
		// System.out.println(entry.getKey());
		// // new states saved
		// players.add(player);
		// found = true;
		// }
		//
		// }

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(playersAbsPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			oos.writeObject(players);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Game.getAllPlayers().remove(player);
	}

	/**
	 * remove Player player from list of players.
	 * 
	 * @param player
	 */
	private void remove(Player player) {
		int i = isPlayer(player.getPlayerName());
		if (i >= 0)
			players.remove(i);
	}

	/**
	 * @return the list of all players as saved in the players file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Player> getPlayers() throws ClassNotFoundException,
			IOException {
		loadAllPlayers();
		return players;
	}

	/**
	 * @param charName
	 * @return whether there exists a player charName in players. -1 if not, >=
	 *         0 otherwise.
	 */
	public int isPlayer(String charName) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerName().equals(charName))
				return i;
		}
		return -1;
	}

	/**
	 * @param index
	 * @return player at a given index
	 */
	public Player getPlayerAt(int index) {
		return players.get(index);
	}

	/**
	 * 
	 * will do the following upon a disconnect: save player find player and set
	 * offline
	 * 
	 * @param playerName
	 */
	public void quitPlayer(String playerName) {
		System.out.println("AccountCollection: player " + playerName + " is quitting; saving player.");
		Player p = Game.findPlayer(playerName);
		findAccount(playerName).setOffline();
		savePlayer(p);
	}

}
