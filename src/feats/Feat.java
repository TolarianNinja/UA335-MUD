package feats;

import java.io.Serializable;

public abstract class Feat implements Serializable {

	private static final long serialVersionUID = 2372711870216579397L;
	private int damageScalar;

	public Feat(int damScal) {
		this.damageScalar = damScal;
	}
	
	
	public abstract int calculateLevel();
	
	/**
	 * @return the damageScalar for this character:
	 * this integer indicates the damage inflicted to
	 * an opponent in a combat situation. This amount will
	 * be subtracted from an opponent's health after
	 * a hit is administered.
	 */
	public int getDamageScalar() {
		return damageScalar;
	}
	
	/**
	 * increase character's damage
	 */
	public void upDamage(int n) {
		if ((damageScalar+n) > 100 || (damageScalar+n) < 0)
			return;
		damageScalar+=n;
	}
	
	
	
	
}
