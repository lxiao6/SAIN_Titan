package edu.drexel.cs.rise.titan.action.repository;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
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

public class AddExsitProjectAction extends AbstractAction{

	protected final  Project proj;
	public AddExsitProjectAction(Project proj, Viewer viewer) {
		
		initialize();
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		AddUI addUI = new AddUI(proj.getRepo());
		refProject curProj = proj.getCurProect();
		refProject proj = new refProject();
		proj.setName(curProj.getName());
		proj.setSize(curProj.getSize());
		proj.setArchLevel(curProj.getArchLevel());
		proj.setArchDepth(curProj.getArchDepth());
		proj.setsMil(curProj.getsMil());
		proj.setgMil(curProj.getgMil());
		proj.setPc(curProj.getPc());
		proj.setArchIssueDir(new File("."));
		addUI.setExistProj(proj);
		addUI.init();
		
	}
	
	private void initialize()
	{
		putValue(NAME, "Add current project");
		putValue(SHORT_DESCRIPTION, "Add new project");
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtilities.CMD_KEY));
	}

}
