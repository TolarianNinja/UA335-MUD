package items;

import model.EquipLocation;
import model.Game;
import model.Item;

public class PortalItem extends Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1199971167981149611L;
	private int destination;

	public PortalItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq, int destination) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		this.destination = destination;
	}
	
	public void use(String charName) {
		Game.findPlayer(charName).move(destination);
	}

}
