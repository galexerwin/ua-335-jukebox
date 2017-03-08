/*Author: Alex Erwin & Ian Burley
 *Purpose: Iteration 1 view for JukeBox GUI
 */
// package definition
package view;
// import classes
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Jukebox;
import model.JukeboxUser;
// gui view implementation 
public class IterationOneView extends JPanel implements Observer {
	// instance variables
	private Jukebox juke;
	private JButton song1;
	private JButton song2;
	private JPanel panel;
	JTextField passField = new JTextField(); 
	JTextField nameField = new JTextField();
	JLabel status = new JLabel();
	String curUser = null;
	JukeboxUser cur;
	// Create the window, adding the song buttons
	public IterationOneView(Jukebox foo, int width, int height) {
		// create a button listener
		ButtonListener buttonList = new ButtonListener();
		// set the layout and size
		this.setLayout(null); 
		this.setSize(width, height);
		// import the model
		juke = foo;
		// song 1 button
		song1=new JButton();
		song1.setText("Select song 1");
		song1.setEnabled(true);
		song1.setSize(200, 30);
		song1.setLocation(20, 20);
		song1.addActionListener(buttonList);
		this.add(song1);
		// song 2 button 
		song2=new JButton();
		song2.setText("Select song 2");
		song2.setEnabled(true);
		song2.setSize(200, 30);
		song2.setLocation(20, 70);
		song2.addActionListener(buttonList);
		this.add(song2);
		// make the panel
		makePanel();
	}
	// create the main panel for 1st iteration
	private void makePanel() {
		// create a button listener
		ButtonListener buttonList=new ButtonListener();
		// new panel and settings
		panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(300, 200);
		panel.setLocation(40, 120);
		panel.setBackground(Color.WHITE);
		// create label
		JLabel name=new JLabel();
		name.setText("Account Name");
		name.setSize(100, 30);
		name.setLocation(50, 50);
		panel.add(name);
		// create label
		JLabel pass=new JLabel();
		pass.setText("Password");
		pass.setSize(200, 30);
		pass.setLocation(50, 80);
		panel.add(pass);
		// name field
		nameField.setText("");
		nameField.setSize(150, 30);
		nameField.setLocation(150, 50);
		panel.add(nameField);
		// password field
		passField.setText("");
		passField.setSize(150, 30);
		passField.setLocation(150, 85);
		panel.add(passField);
		// sign out button
		JButton signOut=new JButton();
		signOut.setEnabled(true);
		signOut.setText("Sign Out");
		signOut.setSize(140, 30);
		signOut.setLocation(10, 120);
		signOut.addActionListener(buttonList);
		panel.add(signOut);
		// login button
		JButton login=new JButton();
		login.setEnabled(true);
		login.setText("Login");
		login.setSize(140, 30);
		login.setLocation(155, 120);
		login.addActionListener(buttonList);
		panel.add(login);
		// status section
		status.setText("Status: 0 played, 25:00:00");
		status.setSize(200, 40);
		status.setLocation(80, 150);
		panel.add(status);
		// add all to the panel
		this.add(panel);
	}
	// updates from the model
	@Override
	public void update(Observable arg0, Object arg1) {} // stub not used yet
	//Button listener, detecting when buttons are pressed
	private class ButtonListener implements ActionListener {
		//take action if button is pressed 
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// get the button clicked
			JButton buttonClicked = (JButton) arg0.getSource();

			// if the user clicks the "Login" button
			if (buttonClicked.getText().equals("Login")) {
				// get the user name as String
				curUser = nameField.getText();
				// get the password as an int
				int pass = Integer.parseInt(passField.getText());
				// log the user in
				juke.userLogin(curUser, pass);
				// if there is a message to the user
				if (juke.getUserMessage().equals("") == false) {
					// get the waiting message
					JOptionPane.showMessageDialog(null, juke.getUserMessage());
					// clear the message 
					juke.clearUserMessage();
				} else {
					// assign a user object to use
					cur = juke.getCurrentUser(curUser);					
				}
			} //end of "Login"
	  
	  		// if the user clicks the "Sign Out" button
			if (buttonClicked.getText().equals("Sign Out")) {
				// logout the user
				juke.userLogout();
				// we should reset the fields on exit
				nameField.setText("");
				passField.setText("");
				// reset status
				status.setText("Status: 0 played, 25:00:00");
			} // end of "Sign Out"
	  
	  		// if the user clicks the "Select song 1" button
			if (buttonClicked.getText().equals("Select song 1")) {
				// add the song to the queue
				juke.addSong("Tada");
				// if there is a message to the user
				if (juke.getUserMessage().equals("") == false) {
					// get the waiting message
					JOptionPane.showMessageDialog(null, juke.getUserMessage());
					// clear the message 
					juke.clearUserMessage();
				}
				// set status if there is a user logged in
				if (!(cur == null) && cur.isLoggedIn())
					status.setText(new lifetimeUsage().calculate(cur));  // I refactored this with an object
			} //end of Song 1
			
			// if the user clicks the "Select song 2" button
			if (buttonClicked.getText().equals("Select song 2")) {
				// add the song to the queue
				juke.addSong("Flute");
				// if there is a message to the user
				if (juke.getUserMessage().equals("") == false) {
					// get the waiting message
					JOptionPane.showMessageDialog(null, juke.getUserMessage());
					// clear the message 
					juke.clearUserMessage();
				}
				// set status if there is a user logged in
				if (!(cur == null) && cur.isLoggedIn())
					status.setText(new lifetimeUsage().calculate(cur));  // I refactored this with an object
			} //end of Song 2
		}   
	}
	/* Ian, I added this for readability and for reuse */
	// lifetime calculations
	private class lifetimeUsage {
		// instance variables
		int lifetimeBalance, songRequests;
		int hours, minutes, seconds;
		// calculate method
		public String calculate(JukeboxUser user) {
			// grab the details
			this.lifetimeBalance = user.getLifetimeMaxBalance();
			this.songRequests 	 = user.getSongRequests();
			// execute calculations
			hours	= lifetimeBalance / 3600;
			minutes	= (lifetimeBalance / 60) % 60;
			seconds	= lifetimeBalance % 60;
			// return
			return "Status: " + this.songRequests + " played, "+hours+":"+minutes+":"+seconds;
		}
	}
}
