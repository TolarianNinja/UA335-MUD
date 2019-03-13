package feats;

public class MobFeat extends Feat {
	private static final long serialVersionUID = 85459974401755204L;

	/**
	 * 
	 */
	public MobFeat() {
		super(5);
	}

	@Override
	public int calculateLevel() {
		return getDamageScalar() / 10;
	}
	
}
