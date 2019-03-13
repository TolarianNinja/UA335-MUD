/**
 * @author Clint Parker
 * To act as intermediary to control the songplayer
 */
package songplayer;
import javax.sound.sampled.*;

public class Controller  {


	private static Control[] controls = null;
	private static SourceDataLine dataLine = null;
	private static String filename = null;
	
	
	public synchronized static void setLine(SourceDataLine line) {
		dataLine = line;
		controls = dataLine.getControls();
	}
	
	public synchronized static void toggleMute() {
		BooleanControl mute = (BooleanControl) controls[1];
		if (mute.getValue())
			mute.setValue(false);
		else
			mute.setValue(true);
	}
	
}
