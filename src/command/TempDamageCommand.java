/**
 * 
 * A temporary TEST command for seeing if 
 * stats persist and work properly
 * 
 * 
 */

package command;

import model.Game;
import controller.Server;

public class TempDamageCommand extends Command<Server> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String character;
	
	public TempDamageCommand(String charName) {
		super(charName);
		this.character = charName;
	}

	@Override
	public void execute(Server server) {
		// TODO Auto-generated method stub
		dealDamage(character);
	}

	public void dealDamage(String character) {
		Game.findPlayer(character).getCharStatus().alterHealth(-10);
	}
	
}
