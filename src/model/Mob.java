package model;

import java.util.Observable;

import controller.ServerOutputObject;

public class Mob extends Character implements Vnum, Hackable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2163387122051600723L;
	private int vnum;
	private boolean canHack;

	public Mob(int vnum, String longDesc, String shortDesc, String roomDesc) {
		super(longDesc, shortDesc, roomDesc);
		this.vnum = vnum;
		this.charStatus = new CharacterStatus(vnum);
		this.canHack = true;
	}

	@Override
	public int getVnum() {
		return vnum;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CharacterStatus && arg instanceof Integer) {
			Integer s = (Integer) arg;

			if (s.equals(vnum)) {
				this.charStatus = (CharacterStatus) o;
				prompt();
			}
		}
	}

	@Override
	public void setHackable(boolean canHack) {
		this.canHack = canHack;
	}

	@Override
	public boolean canHack() {
		return canHack;
	}

	@Override
	public ServerOutputObject hackString() {
		return null;
	}
	
	public void onHack() {
		
	}
}
