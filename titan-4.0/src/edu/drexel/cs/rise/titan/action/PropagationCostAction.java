package edu.drexel.cs.rise.titan.action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.civitas.propagation.PropagationCost;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.util.Digraph;
import edu.drexel.cs.rise.util.WeightedDigraph;

public class PropagationCostAction extends FileAction {

	private static final long serialVersionUID = 10L;
	private static final int height = 100;
	private static final int width = 300;
	protected final Project proj;
	
	protected final Viewer parent;

	public PropagationCostAction(final Project proj, final Viewer parent) {
		super(proj,parent);
		this.proj = proj;
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		putValue(NAME, "Propagation Cost");
		putValue(MNEMONIC_KEY, (int)'P');
		putValue(SHORT_DESCRIPTION, "Calculate Propagation Cost");
		
		final FileFilter csv = new FileNameExtensionFilter("Comma-Seperated Value File (*.csv)", ".csv");
		chooser.addChoosableFileFilter(csv);
		chooser.setFileFilter(csv);
		chooser.setDialogTitle("Export Propagation Costs");
		chooser.setMultiSelectionEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final JDialog dialog = new JDialog(parent, false);
		dialog.setTitle("Propagation Cost");
		dialog.setSize(width, height);
		dialog.setLocationRelativeTo(parent);
		
		final JPanel panel = new JPanel();
		dialog.getContentPane().add(panel);
		
		panel.add(new JLabel("Propagation Cost:"), BorderLayout.EAST);
		
		final JLabel propcost = new JLabel();
		panel.add(propcost, BorderLayout.CENTER);
		final double cost = PropagationCost.calculatePropagationCost(proj.getStructureDependency());
		propcost.setText(Double.toString(cost));
		
		final JButton export = new JButton("Export...");
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				prepare();
				if (showSaveDialog() != JFileChooser.CANCEL_OPTION) {
					final File path = getPathWithExtension(chooser.getSelectedFile(),
							((FileNameExtensionFilter) chooser.getFileFilter())
									.getExtensions()[0]);
					if (!confirmOverwrite(path))
						return;
					export(path);
				}
			}
			
		});
		panel.add(export, BorderLayout.SOUTH);
		dialog.getRootPane().setDefaultButton(export);
		
		dialog.setVisible(true);
	}
	
	private void export(final File path) {
		final WeightedDigraph<String> deps = proj.getStructureDependency();
		final List<String> vars = new ArrayList<String>();
		for (String var : deps.vertices())
			vars.add(var);
		final double[] fanin = PropagationCost.calculateFanInCosts(deps);
		final double[] fanout = PropagationCost.calculateFanOutCosts(deps);
		try {
			final PrintStream test = new PrintStream(new FileOutputStream(path));
			test.println("variables,fanin,fanout");
			for (int i = 0; i < vars.size(); ++i)
				test.println(vars.get(i) + "," + fanin[i] + "," + fanout[i]);
			test.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
