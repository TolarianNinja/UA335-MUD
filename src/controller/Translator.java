/**
 * @author Alex Hartshorn
 * 
 * @description: Used for translating strings into other strings based on character effects.  Singleton.
 */
package controller;

import effects.BinaryEffect;
import effects.DrunkEffect;
import effects.Effect;
import model.Game;

public class Translator {

	/**
	 * 
	 * @param s
	 *            String to be formatted
	 * @return String formatted with first character capitalized.
	 */
	public static String upperFirst(String s) {
		String A = s.charAt(0) + "";
		A = A.toUpperCase();
		String B = s.substring(1, s.length());
		return A + B;
	}

	public static String translateSpeech(String charName, String text) {
		model.Character cha = Game.findPlayer(charName);
		text = upperFirst(text);
		for (Effect e : cha.getCharStatus().getEffects()) {
			if (e instanceof DrunkEffect) {
				text = translateDrunk(text);
			}
			if (e instanceof BinaryEffect) {
				text = translateBinary(text);
			}
		}
		return text;
	}

	public static String translateBinary(String s) {
		String output = "";
		for (Character c : s.toCharArray()) {
			int ascii = (int) c;
			if (c == ' ') {
				output += '_';
			} else if (ascii % 2 == 0) {
				output += '0';
			} else {
				output += '1';
			}
		}
		return output;
	}

	public static String translateDrunk(String s) {
		String output = s;
		
		// specific slurred speech
		output = output.replace("ll", "lLl");
		output = output.replace("ing", "in'");
		output = output.replaceAll("sh", "sHh");
		output = output.replaceAll("tio", "shu");
		
		// 30% chance of capitalizing a letter
		String upperOut = "";
		for (Character cha : output.toCharArray()) {
			if (Math.random() < 0.30) {
				upperOut += Character.toUpperCase(cha);
			} else {
				upperOut += cha;
			}
		}
		output = upperOut;

		// 30% chance of hiccuping at the end of the line
		if (Math.random() < 0.30) {
			output += " *HIC*";
		}
		return output;
	}
}
