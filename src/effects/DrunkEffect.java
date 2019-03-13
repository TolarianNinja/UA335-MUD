package effects;

import java.awt.Color;

import model.Game;

import controller.ServerOutputObject;

public class DrunkEffect extends Effect {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332495080577605791L;

	public DrunkEffect(int intensity, short duration) {
		super(intensity, duration);
	}

	@Override
	protected void calculateEffect() {
	}

	@Override
	public void addEffect(String charName) {
		Game.findPlayer(charName).getCharStatus().addEffect(this);
	}

	@Override
	public ServerOutputObject outputUserBegin() {
		return new ServerOutputObject("You feel tipsy.", Color.WHITE, 1);
	}

	@Override
	public ServerOutputObject outputUserEnd() {
		return new ServerOutputObject("Your body sobers up.", Color.WHITE, 1);
	}

}
