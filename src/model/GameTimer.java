package model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer implements ReceiverSource {
	
	private ArrayList<Receiver> receivers; // collection of interested Receiver objects
	private Timer theTimer;
	private long seconds; // how many seconds since timer started
	private short interval; // number of milliseconds worth of interval
	
	/**
	 * 
	 * @param inInterval - how often the Timer should notify Receivers
	 */
	
	public GameTimer( short inInterval ) {
		interval = inInterval;
		seconds = 0;
		receivers = new ArrayList<Receiver>();
		theTimer = new Timer();
	}
	
	/**
	 * 
	 * @param inInterval - how often the Timer should notify Receivers
	 * @param point - how many seconds since a specified point (i.e. last time it was closed)
	 */
	
	public GameTimer( short inInterval, long point ) {
		this( inInterval );
		seconds = point;
	}
	
	/**
	 * The timer will begin as soon as this method is called. For
	 * every interval, the instantiated class will notify every interested
	 * object that is a Receiver. It is recommended to register as little
	 * Receivers as possible where permitted.
	 */
	
	public void start() {
		
		theTimer.schedule( new TimerTask() {

			@Override
			public void run() {
				++seconds;
				GameTimer.this.signal();
			}
			
		}, 0, interval );
	}
	
	/**
	 * Whenever the client class is done with using GamerTimer, this method
	 * can be called. It will return the time since the timer started or the
	 * specified point.
	 */
	
	public long end() {
		theTimer.cancel();
		return seconds;
	}

	/* (non-Javadoc)
	 * @see model.ReceiverSource#addReceiver(model.Receiver)
	 */
	@Override
	public void addReceiver( Receiver inAddend ) {
		receivers.add( inAddend );
	}
	
	/* (non-Javadoc)
	 * @see model.ReceiverSource#removeReceiver(model.Receiver)
	 */
	@Override
	public void removeReceiver( Receiver inRemovable ) {
		receivers.remove( inRemovable );
	}
	
	/* (non-Javadoc)
	 * @see model.ReceiverSource#signal()
	 */
	
	@Override
	public void signal() {
		for( Receiver current: receivers ) {
			current.update( this );
		}
	}
	
	public long getSeconds() {
		return seconds;
	}
}