package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Jukebox;
import view.IterationOneView;

public class JukeBoxStartGUI extends JFrame{
	private Jukebox foo;
	private IterationOneView view;
	private JPanel currentView;
	
	
	 public static void main(String[] args) {
		    JukeBoxStartGUI g = new JukeBoxStartGUI();
		    g.setVisible(true);
	 }
	 
	 public JukeBoxStartGUI(){		
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setSize(400, 400);
		 Initialize();
		 view=new IterationOneView(foo, 400, 400);
		 foo.addObserver(view);
		 setViewTo(view);
	 }

	private void Initialize() {
		foo=new Jukebox();	
	}
	
	private void setViewTo(JPanel newView) {
	    if (currentView != null)
	      remove(currentView);
	    currentView = newView;
	    add(currentView);
	    currentView.repaint();
	    validate();
	  }
	 
	 

}
