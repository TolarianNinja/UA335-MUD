/**
 * @author Clint Parker
 * simple looper to continuously play the game theme.
 * exactly one instance per client
 */
package songplayer;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.BooleanControl;


public class Looper {
	
	private static Looper sessionLooper = null;
	
	public static Looper getInstanceOf(String filename) {
		if (sessionLooper == null)
			sessionLooper = new Looper(filename);
		return sessionLooper;
	}
	
	private String filename;
	private Looper(String filename) {
		this.filename = filename;
		continuousPlay();
	}
	
	private void continuousPlay() {
		SongPlayer.playFile(new Waiter(), filename);
	}


	private class Waiter implements EndOfSongListener {
		public void songFinishedPlaying(EndOfSongEvent eosEvent) {
			// have a half second delay in between songs
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					continuousPlay();
				}
			}, 550);
		}
	}
}
