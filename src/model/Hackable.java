package model;

import controller.ServerOutputObject;

/**
 * 
 * @author Alex Hartshorn
 * 
 * @description: used on objects and mobs to determine if they can be hacked
 *
 */
public interface Hackable {
	
	public boolean canHack();
	
	public void setHackable(boolean hackable);
	
	public ServerOutputObject hackString();
}
