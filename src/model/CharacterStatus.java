/**
 * @author Clint Parker
 * This class is responsible for containing a character
 * or mob's status -- their health, energy and stamina. 
 * need to notify proper Observer, after one of these fields
 * is modified, to update the prompt in the console.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import effects.Effect;

public class CharacterStatus extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1981450666494592071L;

	/*
	 * attributes for an arbitrary character/mob
	 */
	private int currentHealth;
	private int currentEnergy;
	private int currentStamina;

	private int maxHealth;
	private int maxEnergy;
	private int maxStamina;
	
	private int creditWorth;

	private String playerName; // used for players
	private int vnum; // used for mobs
	
	private ArrayList<Effect> effects;

	/**
	 * Constructor used for players
	 * 
	 * @param playerName
	 */
	public CharacterStatus(String playerName) {
		// default upon construction is 100 for all attributes for players
		this.maxHealth = 100;
		this.maxEnergy = 100;
		this.maxStamina = 100;
		this.creditWorth = 150;
		this.playerName = playerName;
		this.effects = new ArrayList<Effect>();

		regenerate();
	}

	/**
	 * Constructor used for mobs
	 * 
	 * @param vnum
	 */
	public CharacterStatus(int vnum) {
		this.maxHealth = 250;
		this.maxEnergy = 250;
		this.maxStamina = 250;
		this.creditWorth = 250;
		this.vnum = vnum;
		this.effects = new ArrayList<Effect>();

		regenerate();
	}
	
	public void addEffect(Effect e) {
		effects.add(e);
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}

	/**
	 * after a player dies, can reset their health, etc.
	 */
	public void regenerate() {
		this.currentHealth = maxHealth;
		this.currentEnergy = maxEnergy;
		this.currentStamina = maxStamina;

		setChanged();
		notifyObservers();
	}

	/**
	 * alter energy by n. -100 <= n < 0 will decrease energy, 0 < n <= 100
	 * increases
	 * 
	 * @param n
	 */
	public void alterEnergy(int n) {
		if (!validRange(n))
			return;
		if (currentEnergy + n > maxEnergy)
			currentEnergy = maxEnergy;
		else
			currentEnergy += n;
		

		// tell the observer prompt to update the string
		setChanged();
		notifyObservers();
	}

	/**
	 * alter health by n. -100 <= n < 0 will decrease health, 0 < n <= 100
	 * increases
	 * 
	 * @param n
	 */
	public void alterHealth(int n) {
		if (playerName != null) {
			System.out.println("Player " + playerName + " being dealt " + n
					+ " damange [" + currentHealth + "]");
		} else {
			System.out.println(Game.getMob(vnum).getShort()
					+ " is being dealt " + n + " damage [" + currentHealth
					+ "]");
		}
		if (!validRange(n))
			return;
		if (currentHealth + n > maxHealth)
			currentHealth = maxHealth;
		else if( currentHealth + n < 0 )
			currentHealth = 0;
		else
			currentHealth += n;
		setChanged();
		notifyObservers();
	}

	/**
	 * alter stamina by n. -100 <= n < 0 will decrease stamina, 0 < n <= 100
	 * increases
	 * 
	 * @param n
	 */
	public void alterStamina(int n) {
		if (!validRange(n))
			return;
		if (currentStamina + n > maxStamina)
			currentStamina = maxStamina;
		else
			currentStamina += n;
		setChanged();
		notifyObservers();
	}

	/**
	 * set energy
	 * 
	 * @param energy
	 */
	public void setEnergy(int energy) {
		if (!validRange(energy))
			return;
		this.maxEnergy = energy;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return energy level
	 */
	public int getEnergy() {
		return currentEnergy;
	}

	/**
	 * set health level
	 * 
	 * @param health
	 */
	public void setHealth(int health) {
		if (!validRange(health))
			return;
		this.maxHealth = health;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return health level
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * set stamina level
	 * 
	 * @param stamina
	 */
	public void setStamina(int stamina) {
		if (!validRange(stamina))
			return;
		this.maxStamina = stamina;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return stamina level
	 */
	public int getStamina() {
		return currentStamina;
	}

	private boolean validRange(int n) {
		return (n >= -100) && (n <= 100);
	}

	/**
	 * @return a prompt string
	 */
	public String getPrompt() {
		return "<" + this.getCurrentHealth() + "hp " + this.getEnergy()
				+ "energy " + this.getStamina() + "stamina>";
	}
	
	public void changeWorth(int amount) {
		this.creditWorth += amount;
	}
	
	public int getWorth() {
		return creditWorth;
	}
}
