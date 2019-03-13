package effects;

import java.io.Serializable;

import controller.ServerOutputObject;

public abstract class Effect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4191626963963041143L;
	protected int intensity;
	protected Short duration;

	public Effect(int intensity) {
		this.intensity = intensity;
	}

	public Effect(int intensity, short duration) {
		this.intensity = intensity;
		this.duration = duration;
	}

	// calculate what it does based on the input intensity
	protected abstract void calculateEffect();

	// add the effect to the character
	public abstract void addEffect(String charName);

	// the string to send when the effect is activated
	public abstract ServerOutputObject outputUserBegin();

	// the string to send when 
	public abstract ServerOutputObject outputUserEnd();

	// remove the effect from the player, not abstract so that it does not need
	// to be added to effects without duration
	protected void removeEffect(String charName) {
	}

	// update the timer on the player
	public void update(String charName) {
		if (duration != null) {
			if (hasTime()) {
				duration--;
			} else {
				removeEffect(charName);
			}
		}
	}

	// if there is still time left on the effect
	public boolean hasTime() {
		return duration > 0;
	}

}
