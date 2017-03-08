/*Author: Alex Erwin & Ian Burley
 *Purpose: The core of the JukeBoxGUI, setting the view to the 1st iteration.
 */
// package definition
package controller;
// import classes
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Jukebox;
import view.IterationOneView;
// jukebox gui
public class JukeBoxStartGUI extends JFrame {
	// instance variables
	private Jukebox foo;
	private IterationOneView view;
	private JPanel currentView;
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
		this.setSize(400, 400);
		Initialize();
		view = new IterationOneView(foo, 400, 400);
		foo.addObserver(view);
		setViewTo(view);
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
}