package command;

import java.awt.Color;

import model.Character;
import model.Game;
import model.GameTimer;
import model.Player;
import model.Receiver;
import model.ReceiverSource;
import controller.Server;
import controller.ServerOutputObject;

/**
 * The basis for attacks for characters and mobs. The
 * command will describe what's going on and decrease the
 * HP. More will be added later. The precondition is
 * that the characters will use their own strength and
 * body to attack another character. That will be changed
 * later.
 * 
 * @author Christian
 *
 */

public class AttackCommand extends Command<Server> implements Receiver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1422532007611673049L;

	private static final ServerOutputObject NO_ARGS_OUT = new ServerOutputObject( "Attack who?",
			                                                                   Color.WHITE, 
			                                                                   1 );
	
	private static final String PUNCH = " has punched ";
	
	private static final String NO_CHAR_STR = "You don't see ";
	
	public AttackCommand(String charName) {
		super(charName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Server server) {
		
		// there needs to be something in the argument
		if ( arguments.size() != 0 ) {
			
			String victim = arguments.get(0);
			
			// check if the character from the command argument is present
			if (Game.findLocalCharacter(charName, victim) == null) {
				
				// tell user of no such character
				ServerOutputObject none = new ServerOutputObject( NO_CHAR_STR + victim, Color.WHITE, 1 );
				server.sendToUser(charName, none );
			} else {
				Character attacker = Game.findLocalCharacter( charName, charName );
				Character attacked = Game.findLocalCharacter(charName, victim );
				
				/* There will be a procedure that accomplishes the goal of
					modifying the attacked Character's HP. What procedure is used
					is subject to change */
				
				attack( attacker, attacked );
				
				ServerOutputObject result = new ServerOutputObject( charName + PUNCH + victim, Color.ORANGE, 1 );
				
				server.sendToRoom( charName, result );
			}
		} else {
			server.sendToUser( charName, NO_ARGS_OUT );
		}
		
		// code below is reserved for help in modeling combat system
		/**String victim = arguments.get( 0 );
		
		String message = charName + " loves " + victim + ".";
		
		server.sendToRoom(charName, new ServerOutputObject( message, Color.YELLOW, 1 ) );*/
		
	}

	
	/**
	 * A simple method that currently has a hard-coded way of attacking: calculating
	 * damage from attacker's stamina and applying it to attacked's HP. This will be
	 * remodeled later. The precondition is that both characters exist and are in the
	 * same room. The post condition is that the attacked's HP will be lowered.
	 * 
	 * @param attacker
	 * @param attacked
	 */
	private void attack(Character attacker, Character attacked) {
		
		// for now, just punch the attacked
		int stamina = attacker.getCharStatus().getStamina();
		int calculatedDamage = (int) -(stamina*0.15);
		
		attacked.getCharStatus().alterHealth( calculatedDamage );
		
		
	}

	/**
	 * TBA
	 */
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	/** TBA
	 * 
	 */

	@Override
	public void update(ReceiverSource s) {
		
		GameTimer timer = (GameTimer) s;
		
		
	}

}
