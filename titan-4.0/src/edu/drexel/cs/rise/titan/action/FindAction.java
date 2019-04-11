package edu.drexel.cs.rise.titan.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.titan.ui.Find;
import edu.drexel.cs.rise.titan.util.ActionUtilities;

public class FindAction extends AbstractAction {

	private static final long serialVersionUID = 10L;
	
	protected final Viewer parent;
	protected final Project proj;
	
	public FindAction(final Project proj,final Viewer parent)
	{
		this.parent = parent;
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Find...");
		putValue(MNEMONIC_KEY, (int)'F');
		putValue(SHORT_DESCRIPTION, "Find");
		putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionUtilities.CMD_KEY));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Find find = new Find(proj,parent);
		find.setVisible(true);
	}

}
