package edu.drexel.cs.rise.titan.action.repository;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.titan.JGridTable.searchUI;
import edu.drexel.cs.rise.titan.util.ActionUtilities;

public class SearchProjectAction extends AbstractAction{

	private static final long serialVersionUID = 10L;
	protected final  Project proj;
	
	public SearchProjectAction(Project proj, Viewer viewer) {
		// TODO Auto-generated constructor stub
		initialize();
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		

		searchUI searchFrame = new searchUI(proj.getRepo());
		searchFrame.init();
		
		
	}
	
	
	
	private void initialize()
	{
		putValue(NAME, "Search projects");
		putValue(SHORT_DESCRIPTION, "Find a project from the repository");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtilities.CMD_KEY));
	}

}
