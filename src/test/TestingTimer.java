package test;

import model.GameTimer;

public class TestingTimer {

	public static void main( String[] args ) {
		TestTimer a = new TestTimer( 2 );
		TestTimer b = new TestTimer( 4 );
		TestTimer c = new TestTimer( 8 );
		
		GameTimer timer = new GameTimer( (short)1000 );
		
		timer.addReceiver( a );
		timer.addReceiver( b );
		timer.addReceiver( c );
		
		timer.start();
	}
}