package test;

import model.GameTimer;
import model.Receiver;
import model.ReceiverSource;

public class TestTimer implements Receiver {

	private int num;
	
	public TestTimer( int in ){
		num = in;
	}

	@Override
	public void update( ReceiverSource r ) {
		// TODO Auto-generated method stub
		System.out.println( "I am number " + num + "! It is " + ((GameTimer) r).getSeconds() + "!" );
	}

	@Override
	public void update() {
		// do nothing
		
	}

}