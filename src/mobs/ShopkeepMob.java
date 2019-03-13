package mobs;

import model.Mob;

public class ShopkeepMob extends Mob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -92756765832800871L;
	private Double normalPrice = 1.0;
	private Double hackedPrice = 0.15;
	private Double currentSalePrice;
	private Double currentPurchasePrice;

	public ShopkeepMob(int vnum, String longDesc, String shortDesc,
			String roomDesc) {
		super(vnum, longDesc, shortDesc, roomDesc);
		this.currentSalePrice = normalPrice;
		this.currentPurchasePrice = normalPrice / 2;
	}
	
	@Override
	public void onHack() {
		this.currentSalePrice -= hackedPrice;
		this.currentPurchasePrice += hackedPrice;
	}
	
	public void offHack() {
		this.currentSalePrice += hackedPrice;
		this.currentPurchasePrice -= hackedPrice;
	}
	
	public Double getCurrentSalePrice() {
		return currentSalePrice;
	}
	
	public Double getCurrentPurchasePrice() {
		return currentPurchasePrice;
	}
}
