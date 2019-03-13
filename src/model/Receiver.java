package model;

/**
 * A Receiver object, in conjunction with a ReceiverSource implemented
 * object, will know if a source of interest has changed state and will
 * react accordingly (what happens next is at the discretion of the
 * Receiver object.
 * 
 * @author Christian Greyeyes
 *
 */

public interface Receiver {

	/**
	 * In the case that an object that implemented ReceiverSource has to
	 * notify a Receiver of something, the update() method can be implemented
	 * to react however needed be.
	 */
	
	void update();
	
	/**
	 * In the case that an object that implemented ReceiverSource has to
	 * notify a Receiver of something, the update() method can be implemented
	 * to react however needed be. IF need be, the Receiver can know more
	 * information about the ReceiverSource.
	 */
	
	void update( ReceiverSource s );

}