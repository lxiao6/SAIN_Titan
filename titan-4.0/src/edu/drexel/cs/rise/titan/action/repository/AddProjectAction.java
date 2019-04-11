package edu.drexel.cs.rise.titan.action.repository;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.titan.JGridTable.AddUI;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;

public class AddProjectAction extends AbstractAction{
	
	protected final  Project proj;

	public AddProjectAction(Project proj, Viewer viewer) {
		
		initialize();
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Map<String, refProject> temp = new HashMap<>();
		temp.put("project1", null);
		temp.put("project2", null);
		temp.put("project3", null);
		temp.put("project4", null);
		temp.put("project5", null);
		temp.put("project5", null);
		temp.put("project5", null);
		temp.put("project5", null);
		temp.put("project5", null);
		AddUI addUI = new AddUI(proj.getRepo());
		addUI.init();
		
	}
	
	private void initialize()
	{
		putValue(NAME, "Add new project");
		putValue(SHORT_DESCRIPTION, "Add new project");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtilities.CMD_KEY));
	}

}
