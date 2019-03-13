package items;

import model.EquipLocation;
import model.Item;

public class DrinkContainerItem extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String liquidColor;
	private double maxDrinks;
	private double currentDrinks;

	public DrinkContainerItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq, String liquidColor,
			double maxDrinks, double startingDrinks) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		this.liquidColor = liquidColor;
		this.maxDrinks = maxDrinks;
		this.currentDrinks = startingDrinks;
	}

	public DrinkContainerItem(int vnum, String shortDesc, String longDesc,
			String roomDesc, EquipLocation eq, String liquidColor) {
		super(vnum, shortDesc, longDesc, roomDesc, eq);
		this.liquidColor = liquidColor;
		this.maxDrinks = -1;
		this.currentDrinks = 1;
	}
	
	public String getLiquidColor() {
		return liquidColor;
	}

	public String getContents() {
		double remaining = currentDrinks / maxDrinks;
		if (maxDrinks == -1) {
			return getShort() + " has a never ending supply of " + liquidColor
					+ " liquid.";
		} else if (currentDrinks / maxDrinks == 1) {
			return getShort() + " is full of a " + liquidColor + " liquid.";
		} else if (remaining < 1 && remaining >= 0.6) {
			return getShort() + " is more than half full of a " + liquidColor
					+ " liquid.";
		} else if (remaining < 0.6 && remaining >= 0.4) {
			return getShort() + " is about half full of a " + liquidColor
					+ " liquid.";
		} else if (remaining < 0.4 && remaining > 0) {
			return getShort() + " is almost empty.";
		} else {
			return getShort() + " is completely empty.";
		}
	}

	public double getCurrentDrinks() {
		return currentDrinks;
	}

	@Override
	public void use(String charName) {
		if (maxDrinks > -1 && currentDrinks > 0) {
			currentDrinks--;
		}
	}

	public boolean isEmpty() {
		return currentDrinks < 1;
	}
}
