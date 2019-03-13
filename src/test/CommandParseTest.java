package test;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.CommandParser;

public class CommandParseTest {
	
	@Test
	public void testParser() {
		CommandParser comm = new CommandParser("");
//		comm.parse("one argument");		// commented out because it doesn't work like that anymore.
		assertTrue(comm.getCommand().equals("one"));
		assertTrue(comm.numArgs() == 1);
	}
}
