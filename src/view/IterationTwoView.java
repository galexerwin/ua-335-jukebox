/*Author: Alex Erwin & Ian Burley
 *Purpose: Iteration 2 view for JukeBox GUI
 */
// package definition
package view;
//import classes
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.*;
// gui view implementation
@SuppressWarnings("serial")
public class IterationTwoView extends JPanel implements Observer {
	private JTable table;
	private TableModel tmodel;
	private ListModel<JukeboxSong> lmodel;
	private JList<JukeboxSong> list;
	private Jukebox juke;
	JTextField userNameTxt = new JTextField();
	JPasswordField userPassTxt = new JPasswordField(); 
	JLabel status = new JLabel();
	JButton login = new JButton();
	JButton signOut = new JButton();
	private int height, width;
	private Image background;
	// constructor
	public IterationTwoView(Jukebox foo, int width, int height) {
		// import the model
		juke = foo;		
		// set instance variables
		this.width = width;
		this.height = height;
		// set the layout and size
		this.setSize(width, height);
		// get the background image
		getBackgroundImage();
		// initialize the panel
		initializePanel();
	}
	// initialize the panel
	private void initializePanel() {
		// variables
		JPanel tableBlock = getSongsAvailableTable();
		JPanel qlistBlock = getSongsQueuedList();
		JPanel loginBlock = getLoginBlock();
		JPanel tableLblPanel = new JPanel();
		JPanel qlistLblPanel = new JPanel();
		JPanel loginLblPanel = new JPanel();
		JPanel statsLblPanel = new JPanel();
		JLabel tableLabel = new JLabel();
		JLabel qlistLabel = new JLabel();
		JLabel loginLabel = new JLabel();
		JButton moveItemToList = new JButton();
		// set the Song Table label
		tableLabel.setText("Select A Song From The Jukebox");
		tableLabel.setForeground(Color.WHITE);
		tableLabel.setFont(new Font("Serif", Font.BOLD, 24));
		// set the Queue List label
		qlistLabel.setText("Play List (song at the top is playing)");
		qlistLabel.setForeground(Color.WHITE);
		qlistLabel.setFont(new Font("Serif", Font.BOLD, 18));
		// set the Login label
		loginLabel.setText("Account Status");
		loginLabel.setForeground(new Color(161,34,53));
		loginLabel.setFont(new Font("Serif", Font.BOLD, 18));
		// set the Status label
		status.setText(getAccountStatusMessage());
		status.setForeground(new Color(161,34,53));
		status.setFont(new Font("Serif", Font.BOLD, 18));	
		// add each label to a JPanel for contrast
		tableLblPanel.add(tableLabel);
		tableLblPanel.setLocation(10, 10);
		tableLblPanel.setSize(450, 35);
		tableLblPanel.setBackground(new Color(161,34,53,150));
		qlistLblPanel.add(qlistLabel);
		qlistLblPanel.setLocation(10, 545);
		qlistLblPanel.setSize(450, 35);
		qlistLblPanel.setBackground(new Color(161,34,53,150));
		loginLblPanel.add(loginLabel);
		loginLblPanel.setLocation(615, 580);
		loginLblPanel.setSize(450, 35);
		loginLblPanel.setBackground(new Color(255,255,255,200));
		statsLblPanel.add(status);
		statsLblPanel.setLocation(615, 715);
		statsLblPanel.setSize(450, 35);
		statsLblPanel.setBackground(new Color(255,255,255,200));
		// create a button listener
		ButtonListener buttonList = new ButtonListener();			
		// set the button
		moveItemToList.setName("addtoqueue");
		moveItemToList.setEnabled(true);
		moveItemToList.setFont(new Font("Arial", Font.BOLD, 16));
		moveItemToList.setText("Add To Queue");
		moveItemToList.setSize(300, 40);
		moveItemToList.setLocation(90, 485);
		moveItemToList.addActionListener(buttonList);
		// set the layout to null since we are handling absolute positioning
		this.setLayout(null);
		// setup all the blocks to be absolutely positioned and opaque
		// JTABLE block
		tableBlock.setLocation(10, 40);
		tableBlock.setSize(450, 450);
		tableBlock.setOpaque(false);
		// JLIST block
		qlistBlock.setLocation(10, 575);
		qlistBlock.setSize(450, 200);
		qlistBlock.setOpaque(false);		
		// Login Block
		loginBlock.setLocation(615, 615);
		loginBlock.setSize(450, 100);
		loginBlock.setBackground(new Color(161,34,53,150));
		// add all components to the canvas
		this.add(tableLblPanel);
		this.add(qlistLblPanel);
		this.add(loginLblPanel);
		this.add(statsLblPanel);
		this.add(moveItemToList);
		this.add(tableBlock);
		this.add(qlistBlock);
		this.add(loginBlock);
	}
	// the songs available table
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel getSongsAvailableTable() {
		// variables
		JPanel tablePanel = new JPanel();
		// setup the model
		tmodel = juke.getJTableData();
		// setup the table
		table = new JTable();
		table.setModel(tmodel);
		table.setRowSorter(new TableRowSorter(tmodel));		
		// add the scroll pane and table
		tablePanel.add(new JScrollPane(table));
		// return the panel
		return tablePanel;
	}
	// the queued items list
	private JPanel getSongsQueuedList() {
		// variables
		JPanel listPanel = new JPanel();
		JScrollPane scroller;
		// setup the model
		lmodel = juke.getJListData();
		// setup the list
		list = new JList<JukeboxSong>();
		list.setModel(lmodel);
		// define a scroll pane
		scroller = new JScrollPane(list);
		// set the preferred size for the scroll pane
		scroller.setPreferredSize(new Dimension(450,200));
		// add the scroll pane and list
		listPanel.add(scroller);
		// return the panel
		return listPanel;
	}
	// the login block
	private JPanel getLoginBlock() {
		// variables
		JPanel loginPanel = new JPanel();
		JLabel userNameLbl = new JLabel();
		JLabel userPassLbl = new JLabel();
		// create a button listener
		ButtonListener buttonList = new ButtonListener();		
		// set the basics
		loginPanel.setLayout(new GridLayout(3,2,10,10));
		loginPanel.setSize(300, 200);
		loginPanel.setLocation(40, 120);
		// initialize label
		userNameLbl.setText("Account Name");
		userNameLbl.setSize(100, 30);
		userNameLbl.setLocation(50, 50);
		userNameLbl.setForeground(Color.WHITE);
		userNameLbl.setHorizontalAlignment(JLabel.RIGHT);
		// initialize label
		userPassLbl.setText("Password");
		userPassLbl.setSize(200, 30);
		userPassLbl.setLocation(50, 80);
		userPassLbl.setForeground(Color.WHITE);
		userPassLbl.setHorizontalAlignment(JLabel.RIGHT);
		// user name field
		userNameTxt.setText("");
		userNameTxt.setSize(150, 30);
		userNameTxt.setLocation(150, 50);
		// password field
		userPassTxt.setText("");
		userPassTxt.setSize(150, 30);
		userPassTxt.setLocation(150, 85);
		// login button
		login.setName("login");
		login.setEnabled(true);
		login.setText("Login");
		login.setSize(140, 30);
		login.setLocation(155, 120);
		login.addActionListener(buttonList);
		// sign out button
		signOut.setName("logout");
		signOut.setEnabled(false);
		signOut.setText("Sign Out");
		signOut.setSize(140, 30);
		signOut.setLocation(10, 120);
		signOut.addActionListener(buttonList);
		// add items
		loginPanel.add(userNameLbl);
		loginPanel.add(userNameTxt);
		loginPanel.add(userPassLbl);
		loginPanel.add(userPassTxt);
		loginPanel.add(signOut);
		loginPanel.add(login);
		// return the panel
		return loginPanel;
	}
	// the status element's default text
	private String getAccountStatusMessage() {
		// variables
		JukeboxUser whoAmI;
		String loggedInUser = "No one";
		String songsSelected = "0";
		String lifetimeElapsed = "25:00:00";
		// get the user object
		whoAmI = juke.getCurrentUserAsObject();
		// check if anyone is logged in
		if (!(whoAmI == null)) {
			loggedInUser = juke.getCurrentUserAsString();
			songsSelected = Integer.toString(whoAmI.getSongRequests());
			lifetimeElapsed = whoAmI.getLifetimeMaxAsDuration();
		}
		// return the string for the label
		return loggedInUser + " logged in, " + songsSelected + " selected, " + lifetimeElapsed;
	}	
	// handles the background image
	private void getBackgroundImage() {
		// load the background image
		background = new ImageIcon("images/background.png").getImage();
		// get the size 
		Dimension size = new Dimension(this.width, this.height);
		// set the size for image
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	}	
	// paint the canvas
	@Override
	public void paintComponent(Graphics g) {
		// repaint the main window
		super.paintComponent(g);
		// draw background image
		g.drawImage(this.background, 0, 0, null);
	}
	// updates from the model
	@Override
	public void update(Observable arg0, Object arg1) {
		// update the list
		list.setListData(juke.getJListRefresh());
		// update the account status message
		status.setText(getAccountStatusMessage());
	}
	//Button listener, detecting when buttons are pressed
	private class ButtonListener implements ActionListener {
		//take action if button is pressed 
		@Override
		public void actionPerformed(ActionEvent event) {
			// get the button clicked
			JButton buttonClicked = (JButton) event.getSource();
			// get the name of the button clicked
			String buttonName = buttonClicked.getName();
			// if the user clicks the add to queue button
			if (buttonName.equals("addtoqueue")) { this.addItemsToQueue(); }
			// if the user clicks the login button
			if (buttonName.equals("login")) { this.loginUser(); }
			// if the user clicks the login button
			if (buttonName.equals("logout")) { this.logoutUser(); }			
		}
		// detect a login request
		private void loginUser() {			
			// attempt to login the user
			juke.userLogin(userNameTxt.getText(), userPassTxt.getPassword());
			// check status and assign user object
			if (!isThereAnError()) { 
				// enable login button
				login.setEnabled(false);
				// disable logout button
				signOut.setEnabled(true);				
			}	
		}
		// detect a logout request
		private void logoutUser() {
			// logout the user
			juke.userLogout();
			// reset the fields
			userNameTxt.setText("");
			userPassTxt.setText("");
			// enable login button
			login.setEnabled(true);
			// disable logout button
			signOut.setEnabled(false);
		}
		// detect add to queue
		private void addItemsToQueue() {
			// get the selections
			int[] selection = table.getSelectedRows();
			JukeboxLibrary jl = juke.getLibrary();
			// check if there were any selections
			if (selection.length == 0) {
				// check if user is logged in
				if (juke.isUserLoggedIn()) {
					// prompt the user to perform a section
					JOptionPane.showMessageDialog(null, "Select an option first!");
				} else {
					// prompt to login first
					JOptionPane.showMessageDialog(null, "Please login first!");
				}
			} else {
				// iterate over the selections
				for (int i = 0; i < selection.length; i++) {
					// attempt to add the song (convert the row index to the model)
					juke.addSong(jl.getSongKeyAt(table.convertRowIndexToModel(selection[i])));
					// check the status messages
					if (isThereAnError())
						break;
				}
			}		
		}
		// detect an error
		private boolean isThereAnError() {
			// check the status messages
			if (juke.getUserMessage().equals("") == false) {
				// get the waiting message
				JOptionPane.showMessageDialog(null, juke.getUserMessage());
				// clear the message 
				juke.clearUserMessage();
				// end the adding
				return true;
			}
			// return false
			return false;
		}
	}
}
