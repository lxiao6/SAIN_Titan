package edu.drexel.cs.rise.titan.action.show;

import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.ui.MatrixViewer;
import edu.drexel.cs.rise.titan.util.ActionUtilities;

public class ShowHistoryAction extends AbstractAction {
	
	private static final long serialVersionUID = 10L;

	protected final ClusterViewer cluster;
	protected final MatrixViewer matrix;
	protected final Project proj;
	
	public ShowHistoryAction(final Project proj,final ClusterViewer cluster, final MatrixViewer matrix)
	{
		this.cluster = cluster;
		this.matrix = matrix;
		this.proj = proj;
		initialize();
	}
	
	private void initialize()
	{
		putValue(NAME, "Show History Dependency");
		putValue(MNEMONIC_KEY, (int)'R');
	}
	
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		//final Project proj = Project.getInstance();
		final Boolean b = (Boolean) getValue(Action.SELECTED_KEY);
		proj.setHistory(b.booleanValue());
		if(proj.getHistoryDependency() == null){
			warning();
			return;
		}
		if(proj.isQuickdraw())
			ActionUtilities.redraw(proj, cluster, matrix);
	}
	
	protected void warning()
	{
		JOptionPane.showMessageDialog(null, "Please load history dsm!", "Null History" , JOptionPane.WARNING_MESSAGE);
	}

}