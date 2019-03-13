package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class ChatPanel extends JFrame {

	/**
	 *
	 * 
	 * @author Christian Greyeyes
	 */
	private static final long serialVersionUID = -8365489888955264710L;
	
	private JTextPane chatView;
	private JTextField inputField;
	
	private static final int DEFAULT_FONT_SIZE = 12;
	
	public ChatPanel() {
		setUpPanel();
	}
	
	public void toggleVisible( boolean in ) {
		setVisible(in);
	}
	
	public JTextPane getChat() {
		return chatView;
	}

	private void setUpPanel() {
		this.setSize( 640, 480 );
		this.setLayout( null );
		
		chatView = new JTextPane();
		chatView.setEditable( false );
		chatView.setBackground(Color.BLACK);
		
		JScrollPane chatScroll = new JScrollPane( chatView );
		
		inputField = new JTextField();
		inputField.setFont(new Font("Courier New", Font.PLAIN,
				DEFAULT_FONT_SIZE));
		
		inputField.addActionListener(new InputListener());
		
		chatScroll.setSize( 620, 400 );
		chatScroll.setLocation( 5, 5 );
		
		inputField.setSize( 620, 40 );
		inputField.setLocation( 5, 430 );
		
		this.add( chatScroll );
//		this.add( inputField );
		
		//this.setUndecorated( true );
		//getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		this.setVisible( true );
	}
	
	public void update( String user, String message, Color color ) {
		
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, color);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Courier New");
		aset = sc.addAttribute(aset, StyleConstants.Alignment,
				StyleConstants.ALIGN_JUSTIFIED);
		aset = sc
				.addAttribute(aset, StyleConstants.FontSize, DEFAULT_FONT_SIZE);
		
		int front = chatView.getDocument().getLength();
		
		try {
			chatView.getDocument().insertString( front, message, aset );
			chatView.setCaretPosition( front );
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private class InputListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String input = inputField.getText();
			
			inputField.setText( "" );
			
			//client.pushMessage( input );
			
		}
	}
	
}
