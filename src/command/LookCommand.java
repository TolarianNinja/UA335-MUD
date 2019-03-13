package command;

import items.DrinkContainerItem;
import items.ContainerItem;

import java.awt.Color;

import model.EquipLocation;
import model.Game;
import model.Character;
import model.Item;
import controller.Server;
import controller.ServerOutputObject;
import controller.Translator;

public class LookCommand extends Command<Server> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5592548379698819478L;
	private Object target;

	public LookCommand(String charName) {
		super(charName);
	}

	@Override
	public void execute(Server server) {
		target = null;
		if (getArgs().size() > 0)
			target = Game.findLocalTarget(charName, getArgs().get(0));
		if (target != null) {
			server.sendToRoom(charName, outputRoom());
		}
		sendToUser(server);
	}

	private ServerOutputObject outputRoom() {
		if (target instanceof Character)
			return new ServerOutputObject(charName + " looks at "
					+ ((Character) target).getShort() + ".", Color.WHITE, 1);
		else
			return null;
	}

	public void sendToUser(Server server) {
		if (getArgs().size() < 1) {
			server.sendToStream(charName, Game.getPlayerRoom(charName)
					.getName(), Color.magenta, 1);
			server.sendToStream(charName, "   "
					+ Game.getPlayerRoom(charName).getDescription(),
					Color.white, 2);
			server.sendToStream(charName, Game.getPlayerRoom(charName)
					.exitsToString(), Color.green, 1);

			for (Item item : Game.getPlayerRoom(charName).getItems()) {
				String output = "   ";
				if (item.isHacked()) {
					output += "(hacked) " + item.getRoom();
				} else {
					output += item.getRoom();
				}
				server.sendToStream(charName, output,
						Color.white, 1);
			}

			for (Character c : Game.localCharacters(charName)) {
				if (!c.equals(Game.findPlayer(charName))) {
					String output = c.getRoom();
//					if (c.isHacked()) {
//						output += "(hacked) " + c.getRoom();
//					} else {
//						output += c.getRoom();
//					}
					server.sendToStream(charName, output, Color.white, 1);
				}
			}
		} else if (getArgs().size() == 1) {
			if (target != null) {
				if (target instanceof Character) {
					server.sendToStream(charName,
							((Character) target).getLong(), Color.WHITE, 1);
					server.sendToStream(charName,
							((Character) target).getShort()
									+ " is in perfect health.", Color.white, 1);
					showEquipment(charName, (Character) target, server);
				} else if (target instanceof Item) {
					server.sendToStream(charName, ((Item) target).getLong(),
							Color.WHITE, 1);
				}
			} else {
				if (getArgs().get(0).equals("in")) {
					server.sendToStream(charName, "Look in what?", Color.white,
							1);
				} else {
					server.sendToStream(charName, "You don't see that here.",
							Color.WHITE, 1);
				}
			}
		} else if (getArgs().size() == 2 && getArgs().get(0).equals("in")) {
			Item i2 = Game.findLocalItem(charName, getArgs().get(1));
			if (i2 != null) {
				if (i2 instanceof ContainerItem) {
					server.sendToStream(charName, Translator.upperFirst(i2.getShort()) + " contains:",
							Color.WHITE, 1);
					for (Item inside : ((ContainerItem) i2).getInventory()) {
						server.sendToStream(charName,
								"   " + inside.getShort(), Color.WHITE, 1);
					}
				} else if (i2 instanceof DrinkContainerItem) {
					server.sendToStream(charName,
							((DrinkContainerItem) i2).getContents(),
							Color.WHITE, 1);
				} else {
					server.sendToStream(charName,
							"You can't look inside of that.", Color.WHITE, 1);
				}
			} else {
				server.sendToStream(charName, "You aren't carrying that.",
						Color.WHITE, 1);
			}
		} else {
			server.sendToStream(charName, "Just ", Color.WHITE, 0);
			server.sendToStream(charName, "look", Color.magenta, 0);
			server.sendToStream(charName, " at the ", Color.WHITE, 0);
			server.sendToStream(charName, "thing", Color.magenta, 0);
			server.sendToStream(charName, " you want to look at.", Color.WHITE,
					1);
		}
	}

	public void showEquipment(String sender, Character target, Server server) {
		if (target.getEquipment().isEmpty()) {
			server.sendToStream(sender, target.getShort()
					+ " isn't wearing anything.", Color.WHITE, 1);
		}
		for (EquipLocation eq : target.getEquipment().keySet()) {
			if (eq.equals(EquipLocation.HEAD)) {
				server.sendToStream(sender, "<worn on head>        ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.FACE)) {
				server.sendToStream(sender, "<worn on face>        ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.TORSO)) {
				server.sendToStream(sender, "<worn on torso>       ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.ARMS)) {
				server.sendToStream(sender, "<worn on arms>        ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.ON_HANDS)) {
				server.sendToStream(sender, "<worn on hands>       ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.WAIST)) {
				server.sendToStream(sender, "<worn about waist>    ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.LEGS)) {
				server.sendToStream(sender, "<worn on legs>        ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.FEET)) {
				server.sendToStream(sender, "<worn on feet>        ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.HAND_LEFT)) {
				server.sendToStream(sender, "<held in left hand>   ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.HAND_RIGHT)) {
				server.sendToStream(sender, "<held in right hand>  ",
						Color.CYAN, 0);
			}
			if (eq.equals(EquipLocation.FLOATING)) {
				server.sendToStream(sender, "<floating nearby>     ",
						Color.CYAN, 0);
			}
			server.sendToStream(sender, target.getEquipAt(eq).getShort(),
					Color.WHITE, 1);
		}
	}

}
