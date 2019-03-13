package command;

import java.io.Serializable;
import java.util.ArrayList;

import model.Game;

import controller.Server;

/**
 * 
 * @author Gabriel Kishi
 * 
 *         Adapted from NRC Server code demo
 */

@SuppressWarnings("hiding")
public abstract class Command<Server> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4459003931693509929L;
	protected ArrayList<String> arguments = new ArrayList<String>();
	protected String charName;

	public Command(String charName) {
		this.charName = charName;
	}

	public abstract void execute(Server server);

	/**
	 * This is used by CommandParser to set the arguments from the input string.
	 * @param commArgs Arguments from command entered.
	 *  
	 */
	public void setArguments(ArrayList<String> commArgs) {
		arguments = commArgs;
	}

	public ArrayList<String> getArgs() {
		return arguments;
	}

	/**
	 * 
	 * @return Formatted string for say/OOC text.
	 */
	public String getText() {
		String output = "";
		for (String s : getArgs()) {
			output += s + " ";
		}
		return output.trim();
	}

	/**
	 * 
	 * @param s String to be formatted
	 * @return String formatted with first character capitalized. 
	 */
//	public String upperFirst(String s) {
//		String A = s.charAt(0) + "";
//		A = A.toUpperCase();
//		String B = s.substring(1, s.length());
//		return A + B;
//	}
	
	/**
	 * 
	 * @return Formatted string for tell (arg0 is the target)
	 */
	public String getTargetText() {
		String output = "";
		for (int i = 1; i < getArgs().size(); i++) {
			output += getArgs().get(i) + " ";
		}
		return output.trim();
	}
}
