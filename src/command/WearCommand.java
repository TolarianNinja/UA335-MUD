package command;

import java.awt.Color;

import model.EquipLocation;
import model.Game;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;

public class WearCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 141390129463898937L;
	private Item wornItem;
	private String removedShort;

	public WearCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		if (getArgs().size() > 0) {
			wornItem = Game.inPlayerInventory(charName, getArgs().get(0));
		}
		if (wornItem != null
				&& wornItem.getWearLocation() != EquipLocation.INVENTORY) {
			if (Game.findPlayer(charName).getEquipAt(wornItem.getWearLocation()) != null) {
				server.sendToRoom(charName, outputRoomRemove());
				server.sendToUser(charName, outputUserRemove());
			}
			wearItem(server);
			server.sendToRoom(charName, outputRoomWear());
		}
		server.sendToUser(charName, outputUserWear());
	}
	
	private ServerOutputObject outputRoomRemove() {
		return new ServerOutputObject(charName + " stops using "
				+ removedShort + ".", Color.WHITE, 0);
	}
	
	private ServerOutputObject outputUserRemove() {
		return new ServerOutputObject("You stop using " + removedShort + ".",
				Color.WHITE, 1);
	}
	
	private ServerOutputObject outputUserWear() {
		if (getArgs().size() == 0) {
			return new ServerOutputObject( "Wear what?", Color.WHITE, 1);
		} else if (wornItem == null) {
			return new ServerOutputObject( "You aren't carrying that.",
					Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.INVENTORY) {
			return new ServerOutputObject( "You can't wear that.", Color.WHITE,
					1);
		} else if (wornItem.getWearLocation() == EquipLocation.ARMS) {
			return new ServerOutputObject( "You wear " + wornItem.getShort()
					+ " over your arms.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FACE) {
			return new ServerOutputObject( "You put " + wornItem.getShort()
					+ " over your face.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HEAD) {
			return new ServerOutputObject( "You put " + wornItem.getShort()
					+ " on your head.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.TORSO) {
			return new ServerOutputObject( "You wear " + wornItem.getShort()
					+ " over your torso.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.ON_HANDS) {
			return new ServerOutputObject( "You wear " + wornItem.getShort()
					+ " on your hands.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.WAIST) {
			return new ServerOutputObject( "You wrap " + wornItem.getShort()
					+ " around your waist.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.LEGS) {
			return new ServerOutputObject( "You pull " + wornItem.getShort()
					+ " up your legs.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FEET) {
			return new ServerOutputObject( "You wear " + wornItem.getShort()
					+ " on your feet.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HAND_LEFT) {
			return new ServerOutputObject( "You hold " + wornItem.getShort()
					+ " in your left hand.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HAND_RIGHT) {
			return new ServerOutputObject( "You hold " + wornItem.getShort()
					+ " in your right hand.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FLOATING) {
			return new ServerOutputObject( "You release " + wornItem.getShort()
					+ " to float alongside you.", Color.WHITE, 1);
		} else {
			return null;
		}
	}
	
	private ServerOutputObject outputRoomWear() {
		if (wornItem.getWearLocation() == EquipLocation.ARMS) {
			return new ServerOutputObject( 
					charName + " wears " + wornItem.getShort()
							+ " over their arms.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FACE) {
			return new ServerOutputObject( 
					charName + " puts " + wornItem.getShort()
							+ " over their face.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HEAD) {
			return new ServerOutputObject( 
					charName + " puts " + wornItem.getShort()
							+ " on their head.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.TORSO) {
			return new ServerOutputObject( 
					charName + " wears " + wornItem.getShort()
							+ " over their torso.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.ON_HANDS) {
			return new ServerOutputObject( 
					charName + " wears " + wornItem.getShort()
							+ " on their hands.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.WAIST) {
			return new ServerOutputObject( 
					charName + " wraps " + wornItem.getShort()
							+ " around their waist.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.LEGS) {
			return new ServerOutputObject( 
					charName + " pulls " + wornItem.getShort()
							+ " up their legs.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FEET) {
			return new ServerOutputObject( 
					charName + " wears " + wornItem.getShort()
							+ " on their feet.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HAND_LEFT) {
			return new ServerOutputObject( 
					charName + " holds " + wornItem.getShort()
							+ " in their left hand.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.HAND_RIGHT) {
			return new ServerOutputObject( 
					charName + " holds " + wornItem.getShort()
							+ " in their right hand.", Color.WHITE, 1);
		} else if (wornItem.getWearLocation() == EquipLocation.FLOATING) {
			return new ServerOutputObject( 
					charName + " releases " + wornItem.getShort()
							+ " to float alongside them.", Color.WHITE,
					1);
		} else {
			return null;
		}
	}
	
	private void wearItem(Server server) {
		if (Game.findPlayer(charName).getEquipAt(wornItem.getWearLocation()) != null) {
			removeItem(server);
		}
		Game.findPlayer(charName).wearEquipment(getArgs().get(0));
	}

	private void removeItem(Server server) {
		removedShort = Game.inPlayerEquipment(charName, getArgs().get(0)).getShort();
		Game.findPlayer(charName).removeEquipment(getArgs().get(0));
	}
}
