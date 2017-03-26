/*Author: Alex Erwin & Ian Burley
 *Purpose: The core of the JukeBoxGUI, setting the view to the 2nd iteration.
 */
// package definition
package controller;
// import classes
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
// import classes
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Jukebox;
import view.IterationOneView;
import view.IterationTwoView;
// jukebox gui
@SuppressWarnings("serial")
public class JukeBoxStartGUI extends JFrame {
	// instance variables
	private static final String fileName = "JukeboxData";
	private Jukebox foo;
	private IterationOneView view;
	private IterationTwoView xview;
	private JPanel currentView;
	private Jukebox data;
	// new main method
	public static void main(String[] args) {
		// new gui
		JukeBoxStartGUI g = new JukeBoxStartGUI();
		// make it visible
		g.setVisible(true);
	}
	// GUI constructor
	public JukeBoxStartGUI() {
		// define the window properties
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Ichigo's Jukebox Station"); // set title
		this.setSize(1100, 825); // size and location
		this.setLocation(50,50);
		// check if we want to use saved data
		if (keepDataStart() == false) {
			// load the default object
			Initialize();
		} else {
			// load saved object
			foo=data;
			// force the queue to start again
			foo.callNext();
		}
		// define the views
		view = new IterationOneView(foo, 400, 400);
		xview = new IterationTwoView(foo, 1100, 825);
		// add observers
		foo.addObserver(view);
		foo.addObserver(xview);
		// default is the second view
		setViewTo(xview);
		// add a window listener
		this.addWindowListener(new ListenForWindowClose());
	}
	// create new Jukebox to be passed in
	private void Initialize() {
		foo = new Jukebox();	
	}
	// set view, for which iteration
	private void setViewTo(JPanel newView) {
		if (currentView != null)
		  remove(currentView);
		currentView = newView;
		add(currentView);
		currentView.repaint();
		validate();
	}
	// on startup, ask the user if they want to run using the previous save state
	public boolean keepDataStart() {
		// show dialog
		int userInput = JOptionPane.showConfirmDialog(null, "Start with previous save state?");
		// if the answer is yes
		if (userInput == JOptionPane.YES_OPTION) {
			// wrap in try catch
			try (FileInputStream fis = new FileInputStream(fileName); ObjectInputStream ois = new ObjectInputStream(fis);) {
				// grab the data
				data = (Jukebox) ois.readObject();
				// return true
				return true;
			} catch (IOException ioe) {
	    		JOptionPane.showMessageDialog(null, "Reading objects failed");
				//ioe.printStackTrace();
		    } catch (ClassNotFoundException e) {
	    		JOptionPane.showMessageDialog(null, "Class not found");
		    }
		}
		// return default
		return false;	
	}
	// The controller that asks the user to save a persistent object or not
	private class ListenForWindowClose extends WindowAdapter {
		// define our own window close event
		@Override
		public void windowClosing(WindowEvent e) {
			// show dialog
			int userInput = JOptionPane.showConfirmDialog(null, "Save current data?");
			// if the answer is yes
			if (userInput == JOptionPane.YES_OPTION) {
				// logout the current user
				if (foo.isUserLoggedIn() == true)
					foo.userLogout();
				// try saving the data
				try (FileOutputStream fos = new FileOutputStream(fileName); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
					// write the data stream
					oos.writeObject(foo);
					// set the close operation
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch(FileNotFoundException e1) {
		    		JOptionPane.showMessageDialog(null, "File not found");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Error writing objects");
				}
			}
			// if answer is no 
			if (userInput==JOptionPane.NO_OPTION) {
				// set the close operation
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		    // if answer is cancel
		    if (userInput==JOptionPane.CANCEL_OPTION) {
		    	// set the close operation
		    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    }
		}
	}
}