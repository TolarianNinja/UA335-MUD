package model;

import java.util.ArrayList;

/**
 * 
 * @author Alex Hartshorn
 * 
 * @description These are each of the strings that are used in descriptions of
 *              characters and items. Room descriptions are handled differently.
 */

public interface Description {
	/**
	 * 
	 * @return the strings that can be used to interact with the items and
	 *         characters. Usually consists of several words to account for the
	 *         different ways they may be interacted with.
	 */
	public ArrayList<String> getName();

	/**
	 * 
	 * @return the full description when the object is looked at. It is best for
	 *         this to be 4-5 lines long to properly describe it. This may
	 *         include a script to add in line breaks (ie \n) to a single
	 *         string, or this may be changed to be used with StringBuilder.
	 */
	public String getLong();

	/**
	 * 
	 * @return the string that will show in ActionEchos when it is interacted
	 *         with. This is also the string that will be shown when it is seen
	 *         in a character's inventory, equipment, or container.
	 */
	public String getShort();

	/**
	 * 
	 * @return the string that will show when a player enters or looks in a
	 *         room. This will have indicators of how to interact with the
	 *         object, and try to give a picture of what someone would see at a
	 *         glance.
	 */
	public String getRoom();

}
