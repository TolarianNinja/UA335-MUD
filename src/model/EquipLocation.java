package model;

/**
 * 
 * @author Alex Hartshorn
 *
 * @description These are each of the locations where a character 
 * 				can wear items on their body.  Inventory items can
 * 				not be worn.
 */
public enum EquipLocation {
	HEAD,
	FACE,
	TORSO,
	ARMS,
	ON_HANDS,
	WAIST,
	LEGS,
	FEET,
	HAND_LEFT,
	HAND_RIGHT, // weapon hand
	FLOATING,
	INVENTORY
}
