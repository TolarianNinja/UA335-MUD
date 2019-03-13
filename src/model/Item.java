package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.ServerOutputObject;

import effects.Effect;

/**
 * 
 * @author Alex Hartshorn
 * 
 * @description This is an interface with everything that each item is going to
 *              need. Additional attributes will be added from each item type.
 * 
 * @notes Need to figure out adding "use item" interface.
 * 
 */
public class Item implements Description, Vnum, Serializable, Hackable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1228569237519736012L;
	/**
	 * 
	 */
	private int vnum;
	private EquipLocation equipLoc;
	private boolean canPickUp;
	private String shortDesc;
	private String longDesc;
	private String roomDesc;
	private ArrayList<String> names;
	private ArrayList<Effect> effects;
	private boolean canHack;
	private boolean isHacked;
	private int value;

	// super constructor for items
	public Item(int vnum, String shortDesc, String longDesc, String roomDesc,
			EquipLocation eq) {
		this.names = new ArrayList<String>();
		this.effects = new ArrayList<Effect>();
		this.vnum = vnum;
		this.shortDesc = shortDesc;
		this.longDesc = longDesc;
		this.roomDesc = roomDesc;
		this.isHacked = false;
		this.canHack = true;
		this.value = 100;
		if (eq != null) {
			this.canPickUp = true;
			this.equipLoc = eq;
		} else {
			this.canPickUp = false;
		}
	}

	/**
	 * 
	 * @return the EquipLocation that this item can be worn on.
	 */
	public EquipLocation getWearLocation() {
		return equipLoc;
	}

	public void addName(String string) {
		this.names.add(string);
	}

	/**
	 * 
	 * @return if the item can be picked up off the ground. Returns true unless
	 *         the item should remain in the room it is in (see lectern from
	 *         mudexample.txt)
	 */
	public boolean canPickUp() {
		return canPickUp;
	}
	
	public boolean canHack() {
		return canHack;
	}
	
	@Override
	public void setHackable(boolean hackable) {
		this.canHack = hackable;
	}
	
	public boolean isHacked() {
		return isHacked;
	}
	
	public void setHacked(boolean hacked) {
		this.isHacked = hacked;
	}
	
	@Override
	public ServerOutputObject hackString() {
		return null;
	}
	
	public void use(String charName) {
		
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}
	
	public void addEffect(Effect e) {
		this.effects.add(e);
	}
	
	public void applyEffects(String charName) {
		for (Effect ef : effects) {
			ef.addEffect(charName);
		}
	}

	@Override
	public int getVnum() {
		return vnum;
	}

	@Override
	public ArrayList<String> getName() {
		return names;
	}

	@Override
	public String getLong() {
		return longDesc;
	}

	@Override
	public String getShort() {
		return shortDesc;
	}

	@Override
	public String getRoom() {
		return roomDesc;
	}

	public int getValue() {
		return value;
	}
}
