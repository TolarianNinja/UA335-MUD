package items;

import model.EquipLocation;
import model.Item;

public class EdibleItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6065202827488582766L;

	public EdibleItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		
	}
	
	@Override
	public void use(String charName) {
		
	}
}
