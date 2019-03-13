package model;

/**
 * 
 * @author Alex Hartshorn
 * 
 * @description The VirtualNumber (vnum) is a number that is used to keep track
 *              of each individual object in the game. Each room, mob, and item
 *              will be assigned a vnum. These are used as identifiers for ease
 *              of interaction, instead of trying to track more complex
 *              interactions with strings. They could also be used as keys in a
 *              map.
 * 
 */
public interface Vnum {

	/**
	 * 
	 * @return the vnum of the object in question. This is less about having it
	 *         return and more about making sure that each room/mob/item has one
	 *         assigned to it.
	 */
	public int getVnum();

}
