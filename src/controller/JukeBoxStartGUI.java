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
import view.IterationTwoView;
// jukebox gui
public class JukeBoxStartGUI extends JFrame {
	// instance variables
	private Jukebox foo;
	private IterationOneView view;
	private IterationTwoView xview;
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
		this.setTitle("Ichigo's Jukebox Station");
		this.setSize(1100, 825);
		this.setLocation(50,50);
		Initialize();
		
		view = new IterationOneView(foo, 400, 400);
		xview = new IterationTwoView(foo, 1100, 825);
		foo.addObserver(view);
		foo.addObserver(xview);
		setViewTo(xview);
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