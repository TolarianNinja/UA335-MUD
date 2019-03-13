package model;

/**
 *  An object implementing ReceiverSource has the ability to keep track of Receiver
 *  objects and notify them of any changes in the state where desired. This class
 *  follows the observer design pattern.
 *  
 *  @author Christian Greyeyes
 *  
 */

public interface ReceiverSource {

	public abstract void addReceiver(Receiver inAddend);

	public abstract void removeReceiver(Receiver inRemovable);

	/**
	 *  When something of interest occurs, the implemented objects
	 *  can simply let interested Receivers (a list of Receivers kept)
	 *  know with this method.
	 *  
	 */

	public abstract void signal();

}