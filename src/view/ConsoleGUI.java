package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import controller.ServerOutputObject;
import songplayer.*;

public class ConsoleGUI extends JFrame {
	private static final long serialVersionUID = 7175105549042707751L;
	private JTextPane consoleView;
	private JScrollPane consoleScroll;
	private JTextField inputField;
	private JButton hideButton;
	
	private String name;
	
	
	private static String base = System.getProperty("user.dir")
			+ System.getProperty("file.separator");
	
	private List<Object> serverMessages;
	private Socket server;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String host;
	private String port;
	private Queue<String> clientMessageHistory;

	private static final int DEFAULT_FONT_SIZE = 12;
	private static final String NEWLINE = "\n";
	private ChatPanel chatWindow;

	public ConsoleGUI() {
		// prompt = "<100hp  100mana  100mv> ";
		serverMessages = new ArrayList<Object>();
		clientMessageHistory = new LinkedList<String>();

		try {
			// prompt user for information
			host = JOptionPane.showInputDialog("Host address:");
			port = JOptionPane.showInputDialog("Host port:");

			// initialize a new socket with given host and port
			server = new Socket(host, Integer.parseInt(port));
			out = new ObjectOutputStream(server.getOutputStream());
			in = new ObjectInputStream(server.getInputStream());

			
			name = "";
			
			
			setUpGUI();
			new Thread(new ServerHandler()).start();
			
			setUpChatPanel();
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	private class CollapsedControlPanel extends JPanel {
		private JButton expand;
		private ImageIcon expandIcon;
		
		public CollapsedControlPanel() {
			expandIcon = new ImageIcon(base + "icons" + System.getProperty("file.separator") + "expand_scaled0.png");
			expand = new JButton(expandIcon);
			
//			expand.setFocusPainted(false);
			expand.setMargin(new Insets(0,0,0,0));
			expand.setContentAreaFilled(false);
			expand.setBorderPainted(false);
			expand.setOpaque(false);
			
			
			
			this.add(expand);
		}
		
		JButton getExpandButton() {
			return expand;
		}
	}
	
	private class ExpandedPanel extends JPanel {
		private JButton collapse;
		private ImageIcon collIcon;
		private JButton logout;
		private ImageIcon logIcon;
		private JButton mute;
		private ImageIcon mutIcon;
		private JButton unmute;
		private ImageIcon unmIcon;
		
		public ExpandedPanel() {
			this.setLayout(new GridLayout(3, 1));
			
			/*icons and buttons initializations */
			collIcon = new ImageIcon(base + "icons" + System.getProperty("file.separator") + "collapse_scaled0.png");
			collapse = new JButton(collIcon);
			
			logIcon = new ImageIcon(base + "icons" + System.getProperty("file.separator") + "logout_32.png");
			logout = new JButton(logIcon);
			
			mutIcon = new ImageIcon(base + "icons" + System.getProperty("file.separator") + "speaker_off_32.png");
			mute = new JButton(mutIcon);
			
			unmIcon = new ImageIcon(base + "icons" + System.getProperty("file.separator") + "sound_high32.png");
			unmute = new JButton(unmIcon);
			/* end*/
			
			decorate(collapse);
			decorate(logout);
			decorate(unmute);
			decorate(mute);
			
			/* establish listeners */
			logout.addActionListener(new ActionListener() {
				@Override
				public synchronized void actionPerformed(ActionEvent e) {
					System.out.println("clicked, name " + name + " " + name.length());
					if (name.length() > 0)
						try {
							out.writeObject("quit");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}
			});
			
			this.mute.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					toggleSound();
				}
			});
			
			this.unmute.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toggleSound();
				}
			});
			/* end listener setup */
			
			this.unmute.setVisible(false);
			
			this.add(collapse);
			this.add(logout);
			// the music will play by default, so display mute as option
			this.add(mute);
		}
		
		/**
		 * toggle sound icon
		 */
		private void toggleSound() {
			System.out.println("opted toggle sound => ismuted " + mute.isVisible());
			if (mute.isVisible()) {
				mute.setVisible(false);
				this.remove(mute);
				unmute.setVisible(true);
				this.add(unmute);
				Controller.toggleMute();
			} else {
				unmute.setVisible(false);
				this.remove(unmute);
				mute.setVisible(true);
				this.add(mute);
				Controller.toggleMute();
			}
		}
		
		JButton getCollapseButton() {
			return collapse;
		}
		
		
		void decorate(JButton butt) {
			butt.setMargin(new Insets(0,0,0,0));
			butt.setContentAreaFilled(false);
			butt.setBorderPainted(false);
			butt.setOpaque(false);
		}
	}

	/**
	 * 
	 * @author clint
	 * control panel that currently includes the following options:
	 *  * logout
	 *  * mute
	 *  * unmute
	 *  * collapse and expand
	 */
	private class ControlPanel extends JPanel {
		
		private JButton expand;
		private JButton collapse;
		private CollapsedControlPanel ccp;
		private ExpandedPanel exp;
		
		public ControlPanel() {
			// by default, upon launch, panel will be collapsed
			ccp = new CollapsedControlPanel();
			ccp.setVisible(true);
			this.add(ccp);
			
			exp = new ExpandedPanel();
			exp.setVisible(false);
			this.add(exp);
			
			collapse = exp.getCollapseButton();
			collapse.addActionListener(new CollListen());
			expand = ccp.getExpandButton();
			expand.addActionListener(new ExpandListen());
			
			this.setVisible(true);
		}
		
		private class CollListen implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleCollapse();
			}
		}
		
		/**
		 * listen for wanting to toggle panel
		 */
		private class ExpandListen implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleExpand();
			}

		}
		
		private void toggleCollapse() {
			exp.setVisible(false);
			ccp.setVisible(true);
		}
		
		private void toggleExpand() {
			ccp.setVisible(false);
			exp.setVisible(true);
		}
		
	}
	
	
	
	private class ServerHandler implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					
					// read replies from server...
					Object serverOb = null;
					
					try {
						serverOb = in.readObject();
					} catch (Exception e) {
					}
					
					if (serverOb != null) {
						ServerOutputObject s = (ServerOutputObject) serverOb;
					
						serverMessages.add(s.getString());
						appendToPane(consoleView, s.getString(), s.getColor());
					}
//			} catch (SocketException e) {
//				return; // "gracefully" terminate after disconnect
//			} 
					// read replies from server... 
					try {
					ServerOutputObject s = (ServerOutputObject) in.readObject();
					appendToPane(consoleView, s.getString(), s.getColor());
					
					
					System.out.println( s.isChat() );
					
					if( s.isChat() ) {
						appendToPane(chatWindow.getChat(), s.getString(), s.getColor());
					}
					} catch (Exception EOFException) {
						// after 2.5s of delay,  get rid of this GUI
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								System.exit(0);
							}
						};
						timer.schedule(task, 2500);
						break;
					}
					
					
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void setUpGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width / 2, screenSize.height / 2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setTitle("NinjaMUD");
		this.setLocationRelativeTo(null);

		consoleView = new JTextPane();
		consoleView.setEditable(false);
		consoleView.setBackground(Color.BLACK);

		

		consoleScroll = new JScrollPane(consoleView);

		inputField = new JTextField();
		inputField.setFont(new Font("Courier New", Font.PLAIN,
				DEFAULT_FONT_SIZE));
		inputField.addActionListener(new InputListener());

		this.add(consoleScroll, BorderLayout.CENTER);
		this.add(inputField, BorderLayout.PAGE_END);
		this.add(new ControlPanel(), BorderLayout.EAST);
		
		Looper.getInstanceOf(base + "fleetingmemories.mp3");
	}
	
	public void setUpChatPanel() {
		chatWindow = new ChatPanel();
	}

	
	private void appendToPane(JTextPane tp, String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Courier New");
		aset = sc.addAttribute(aset, StyleConstants.Alignment,
				StyleConstants.ALIGN_JUSTIFIED);
		aset = sc
				.addAttribute(aset, StyleConstants.FontSize, DEFAULT_FONT_SIZE);

		int len = tp.getDocument().getLength();

		try {
			tp.getDocument().insertString(len, msg, aset);
			len = tp.getDocument().getLength();
			tp.setCaretPosition(len);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void update(List<String> messages) {
		String s = "";
		for (String message : messages)
			s = s + message + "\n";

		consoleView.setText(s);
		consoleView.setCaretPosition(s.length());
		repaint();
	}

	public void showPrompt(String promptString) {
		appendToPane(consoleView, promptString, Color.CYAN);
	}

	// write object to server
	public void writeToServer() throws IOException {
		out.writeObject((String) clientMessageHistory.poll());
	}
	
	/**
	 * check if the user logged in
	 * @return character name -- may be undefined
	 */
	public synchronized String checkIfLogin() {
		String potentialCharName = "";
		String theMessage = "";
		for (int i = 0; i < serverMessages.size(); i++) {
			if (((String)serverMessages.get(i)).contains("Valid username,")) {
				theMessage = (String) serverMessages.get(i);
				int a = theMessage.indexOf('<');
				int b = theMessage.indexOf('>');
				potentialCharName = theMessage.substring(a+1, b);
				
			}
		}
		return potentialCharName;
	}

	private class InputListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String input = inputField.getText();
			appendToPane(consoleView, input + NEWLINE, Color.YELLOW);
			inputField.setText("");

			name = checkIfLogin();
			
			// keep a log of message history
			clientMessageHistory.add(input);

			try {
				writeToServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConsoleGUI gui = new ConsoleGUI();
		gui.setVisible(true);
	}


}