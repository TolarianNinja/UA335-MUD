package controller;

import java.awt.Color;
import java.io.Serializable;

public class ServerOutputObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1206041819781925373L;
	private String string;
	private Color color;
	private boolean isChat;
	private static final String NEWLINE = "\n";

	public ServerOutputObject(String string, Color color, int newLines) {
		this.string = string;
		this.color = color;
		this.isChat = false;

		for (int i = 0; i < newLines; i++) {
			this.string += NEWLINE;
		}
	}
	
	public ServerOutputObject(String string, Color color, int newLines, boolean isChat) {
		this.string = string;
		this.color = color;
		this.isChat = isChat;

		for (int i = 0; i < newLines; i++) {
			this.string += NEWLINE;
		}
	}
	
	public String getString() {
		return string;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean isChat() {
		return isChat;
	}
}