/*Author: Alex Erwin & Ian Burley
 *Purpose: The core of the JukeBoxGUI, setting the view to the 1st iteration.
 */
// package definition
package controller;
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
import model.JukeboxLibrary;
import view.IterationOneView;
import view.IterationTwoView;
// jukebox gui
public class JukeBoxStartGUI extends JFrame {
	// instance variables
	private Jukebox foo;
	private IterationOneView view;
	private IterationTwoView xview;
	private JPanel currentView;

	private String fileName;
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Ichigo's Jukebox Station");
		this.setSize(1100, 825);
		this.setLocation(50,50);
		fileName="JukeboxData";
		
		
		if (keepDataStart()==false)
			Initialize();
		else{
			foo=data;
			foo.callNext();
		}
		
		view = new IterationOneView(foo, 400, 400);
		xview = new IterationTwoView(foo, 1100, 825);
		foo.addObserver(view);
		foo.addObserver(xview);
		setViewTo(xview);
		this.addWindowListener(new ListenForWindowClose());
	}
	// create new Jukebox to be passed in
	private void Initialize() {
		foo=new Jukebox();	
	}
	//set view, for which iteration
	private void setViewTo(JPanel newView) {
		if (currentView != null)
		  remove(currentView);
		currentView = newView;
		add(currentView);
		currentView.repaint();
		validate();
	}
	
	//On startup, ask the user if they want to run using the previous save state
	public boolean keepDataStart(){
		int userInput=JOptionPane.showConfirmDialog(null, "Start with previous save state?");
		if (userInput==JOptionPane.NO_OPTION){
			return false;
		}
		
		if (userInput==JOptionPane.CANCEL_OPTION){
			return false;
		}
		
		if (userInput==JOptionPane.YES_OPTION){
			
			try{
				FileInputStream fis=new FileInputStream(fileName);
				ObjectInputStream ois=new ObjectInputStream(fis);
				data=(Jukebox) ois.readObject();
				ois.close();
				return true;
			} catch (IOException ioe) {
	    		JOptionPane.showMessageDialog(null, "Reading objects failed");
				//ioe.printStackTrace();
		    } catch (ClassNotFoundException e) {
	    		JOptionPane.showMessageDialog(null, "Class not found");
		    }
		}
		return false;		
	}
	
	 // The controller that asks the user to save a persistent object or not
	  private class ListenForWindowClose extends WindowAdapter {

	    @Override
	    public void windowClosing(WindowEvent e) {
	    	 int userInput=JOptionPane.showConfirmDialog(null, "Save current data?");
	    	  if (userInput==JOptionPane.NO_OPTION){
	    		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	    	return;
	    	    }
	    	    
	    	    if (userInput==JOptionPane.CANCEL_OPTION){
	    	    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    	    }
	    	    
	    	    if (userInput==JOptionPane.YES_OPTION){
	    	    	if(foo.isUserLoggedIn()==true)
	    	    		foo.userLogout();
	    	    	FileOutputStream fos;
	    	    	ObjectOutputStream oos;
	    	    	try{
	    	    		fos=new FileOutputStream(fileName);
	    	    		oos=new ObjectOutputStream(fos);
	    	    		oos.writeObject(foo);
	    	    		oos.close();
	    	    		fos.close();
	  	    		  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    	    	}
	    	    	catch(FileNotFoundException e1){
	    	    		JOptionPane.showMessageDialog(null, "File not found");
	    	    	} catch (IOException e1) {
	    	    		//e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error writing objects");
					}
	    	    }
	    }
	  }
}