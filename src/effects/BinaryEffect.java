package effects;

import java.awt.Color;

import model.Game;

import controller.ServerOutputObject;

public class BinaryEffect extends Effect {

	/**
	 * Mostly empty because most of the work is done in controller.Translator
	 * class. Class is mostly done for instantiation purposes.
	 */
	private static final long serialVersionUID = 4847979272110021948L;

	public BinaryEffect(int intensity, short duration) {
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
		return new ServerOutputObject(
				"Your speech center begins outputting machine language.",
				Color.blue, 1);
	}

	@Override
	public ServerOutputObject outputUserEnd() {
		return new ServerOutputObject("Your speech center returns to normal.",
				Color.WHITE, 1);
	}

}
