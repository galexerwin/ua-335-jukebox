package controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Jukebox;
import view.IterationOneView;

/*The core of the JukeBoxGUI, setting the view to the 1st iteration.
 * 
 *
 */
public class JukeBoxStartGUI extends JFrame{
	private Jukebox foo;
	private IterationOneView view;
	private JPanel currentView;
	
	//create new GUI, make it visible
	 public static void main(String[] args) {
		    JukeBoxStartGUI g = new JukeBoxStartGUI();
		    g.setVisible(true);
	 }
	 
	 //GUI constructor
	 public JukeBoxStartGUI(){		
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setSize(400, 400);
		 Initialize();
		 view=new IterationOneView(foo, 400, 400);
		 foo.addObserver(view);
		 setViewTo(view);
	 }

	 //create new Jukebox to be passed in
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
