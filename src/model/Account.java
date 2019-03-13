package model;

import java.io.Serializable;

/**
 * 
 * @author Clint Parker This class describes an Account object that stores
 *         relevant character information
 */
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 836006147740757788L;
	private String password;
	private String username;
	private boolean isOnline;

	// private String savedGamePath;

	/**
	 * @param username
	 * @param password
	 */
	public Account(String username, String password) {
		this.password = password;
		this.username = username;
		isOnline = false;
	}

	/**
	 * @return character's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * persist this account
	 * 
	 */
	protected void persist() {
		// TODO
	}

	// public String getSavedGamePath() {
	// return savedGamePath;
	// }

	/**
	 * @return character's username
	 */
	public String getCharacterName() {
		return username;
	}

	/**
	 * logon to this account
	 */
	protected void logon() {
		isOnline = true;
	}

	/**
	 * logoff this account
	 */
	protected void logoff() {
		isOnline = false;
	}

	/**
	 * @return location of where to locally save this accounts data.
	 */
	protected String getSaveLocation() {
		return System.getProperty("user.dir")
				+ System.getProperty("file.separator") + username + ".data";
	}

	/**
	 * @return whether or not this account, associated with character, is online
	 */
	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline() {
		isOnline = true;
	}

	public void setOffline() {
		isOnline = false;
		
	}

}
