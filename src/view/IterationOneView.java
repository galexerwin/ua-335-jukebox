package view;

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

/*Iteration 1 view for JukeBox GUI
 * 
 * 
 */
public class IterationOneView extends JPanel implements Observer{
	private Jukebox juke;
	private JButton song1;
	private JButton song2;
	private JPanel panel;

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	//Create the window, adding the song buttons
	public IterationOneView(Jukebox foo, int width, int height){
		 ButtonListener buttonList=new ButtonListener();
		 this.setLayout(null); 
		 this.setSize(width, height);
		 juke=foo;
		 song1=new JButton();
		 song1.setText("Select song 1");
		 song1.setEnabled(true);
		 song1.setSize(200, 30);
		 song1.setLocation(20, 20);
		 song1.addActionListener(buttonList);
		 this.add(song1);
		 
		 song2=new JButton();
		 song2.setText("Select song 2");
		 song2.setEnabled(true);
		 song2.setSize(200, 30);
		 song2.setLocation(20, 70);
		 song2.addActionListener(buttonList);
		 this.add(song2);
		 makePanel();
		 //this.add(master);
	}
	
	JTextField passField=new JTextField(); 
	JTextField nameField=new JTextField();
	JLabel status=new JLabel();
	String curUser=null;
	JukeboxUser cur;

	//create the main panel for 1st iteration
	private void makePanel() {
		ButtonListener buttonList=new ButtonListener();
		panel=new JPanel();
		panel.setLayout(null);
		panel.setSize(300, 200);
		panel.setLocation(40, 120);
		panel.setBackground(Color.WHITE);
		JLabel name=new JLabel();
		name.setText("Account Name");
		name.setSize(100, 30);
		name.setLocation(50, 50);
		panel.add(name);
		
		JLabel pass=new JLabel();
		pass.setText("Password");
		pass.setSize(200, 30);
		pass.setLocation(50, 80);
		panel.add(pass);
	
		nameField.setText("");
		nameField.setSize(150, 30);
		nameField.setLocation(150, 50);
		panel.add(nameField);
		
		passField.setText("");
		passField.setSize(150, 30);
		passField.setLocation(150, 85);
		panel.add(passField);
		
		JButton signOut=new JButton();
		signOut.setEnabled(true);
		signOut.setText("Sign Out");
		signOut.setSize(140, 30);
		signOut.setLocation(10, 120);
		signOut.addActionListener(buttonList);

		panel.add(signOut);
		
		JButton login=new JButton();
		login.setEnabled(true);
		login.setText("Login");
		login.setSize(140, 30);
		login.setLocation(155, 120);
		login.addActionListener(buttonList);
		panel.add(login);
		
		
		status.setText("Status: 0 played, 25:00:00");
		status.setSize(200, 40);
		status.setLocation(80, 150);
		panel.add(status);
		this.add(panel);
	}
	
	//Button listener, detecting when buttons are pressed
	private class ButtonListener implements ActionListener {
	    @Override
	    
	    //take action if button is pressed 
	    public void actionPerformed(ActionEvent arg0) {
	      JButton buttonClicked = (JButton) arg0.getSource();
	      if (buttonClicked.getText().equals("Login")){
	    	  curUser=nameField.getText();
	    	  cur=juke.getCurrentUser(curUser);
	    	  int pass=Integer.parseInt(passField.getText());
	    	  juke.userLogin(curUser, pass);
	    	  if (juke.getUserMessage().equals("Invalid Username or Password"))
	    		  JOptionPane.showMessageDialog(null, juke.getUserMessage());

	      }//end of "Login"
	      
	      if (buttonClicked.getText().equals("Sign Out")){
	    	  juke.userLogout();
	    	  status.setText("Status: 0 played, 25:00:00");

	      }// end of "Sign Out"
	      
	      if (buttonClicked.getText().equals("Select song 1")){
	    	  juke.addSong("Tada");
	    	  if (juke.getUserMessage().equals("")==false)
	    		  JOptionPane.showMessageDialog(null, juke.getUserMessage());
	    	  int totalSeconds=cur.getLifetimeMaxBalance();
	    	  int hours=totalSeconds/3600;
	    	  int minutes=(totalSeconds/60)%60;
	    	  int seconds=totalSeconds%60;
	    	  status.setText("Status: "+cur.getSongRequests()+" played, "+hours+":"+minutes+":"+seconds);
	      }//end of Song 1
	      
	      if (buttonClicked.getText().equals("Select song 2")){
	    	  juke.addSong("Flute");
	    	  if (juke.getUserMessage().equals("")==false)
	    		  JOptionPane.showMessageDialog(null, juke.getUserMessage());
	    	  int totalSeconds=cur.getLifetimeMaxBalance();
	    	  int hours=totalSeconds/3600;
	    	  int minutes=(totalSeconds/60)%60;
	    	  int seconds=totalSeconds%60;
	    	  status.setText("Status: "+cur.getSongRequests()+" played, "+hours+":"+minutes+":"+seconds);
	      }//end of Song 2
	    }
	    
	}
	
	

}
