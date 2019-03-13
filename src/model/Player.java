package model;

import java.io.Serializable;
import java.util.Observable;

import feats.CharacterFeat;

public class Player extends Character implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8524009079023690188L;
	private String name;

	public Player(String name) {
		super("You don't see anything special about them.", name, name
				+ " is here.");
		names.add(name);
		this.name = name;
		this.charStatus = new CharacterStatus(name);
		this.charFeat = new CharacterFeat();
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CharacterStatus && arg instanceof String) {
			String s = (String) arg;

			if (s.equals(charName)) {
				this.charStatus = (CharacterStatus) o;
				prompt();
			}
		}
	}
}
