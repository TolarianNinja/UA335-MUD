package effects;

import java.awt.Color;

import controller.ServerOutputObject;
import model.Game;

public class StatEffect extends Effect {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1749191327242807877L;
	private String stat;
	private int value;

	public StatEffect(int intensity, String stat) {
		super(intensity);
		this.stat = stat;
		calculateEffect();
	}

	@Override
	protected void calculateEffect() {
		value = intensity * 2;
	}

	// based on the stat that is input, alter the character stat by the value
	// calculated
	@Override
	public void addEffect(String charName) {
		if (stat.equals("hp")) {
			Game.findPlayer(charName).getCharStatus().alterHealth(value);
		} else if (stat.equals("energy")) {
			Game.findPlayer(charName).getCharStatus().alterEnergy(value);
		} else if (stat.equals("stamina")) {
			Game.findPlayer(charName).getCharStatus().alterStamina(value);
		}
	}

	// determine which string is sent in which color based on which effect is
	// given, testing against stat and value
	@Override
	public ServerOutputObject outputUserBegin() {
		if (intensity < 0) {
			if (stat.equals("hp")) {
				return new ServerOutputObject("Wounds spontaneously open ",
						Color.red, 1);
			} else if (stat.equals("energy")) {
				return new ServerOutputObject("Energy drains from your body.",
						Color.red, 1);
			} else if (stat.equals("stamina")) {
				return new ServerOutputObject("Your body feels tired.",
						Color.RED, 1);
			} else {
				return null;
			}
		} else {
			if (stat.equals("hp")) {
				return new ServerOutputObject(
						"Your healing is accelerated for a moment.",
						Color.green, 1);
			} else if (stat.equals("energy")) {
				return new ServerOutputObject("Your body fills with energy.",
						Color.green, 1);
			} else if (stat.equals("stamina")) {
				return new ServerOutputObject("Your body feels refreshed.",
						Color.green, 1);
			} else {
				return null;
			}
		}
	}

	// null because it is an immediate effect
	@Override
	public ServerOutputObject outputUserEnd() {
		return null;
	}
}
