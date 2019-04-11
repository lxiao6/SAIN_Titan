package edu.drexel.cs.rise.titan.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;

public final class Find extends JDialog implements ActionListener {

	private static final long serialVersionUID = 10L;
	private static final int height = 100;
	private static final int width = 300;
	
	protected final Project proj;
	private final Viewer frame;
	private final JTextField text;
	private final JPanel panel;
	private final JButton find = new JButton("find");
	private final JButton cancel = new JButton("cancel");
	//private final JButton next = new JButton("next");
	//private final JButton previous = new JButton("previous");
	private final JCheckBox casesensitive = new JCheckBox("casesensitive");
	
	public Find(final Project proj,final Viewer frame) {
		super(frame, false);
		this.frame = frame;
		this.proj = proj;
		
		panel = new JPanel();
		getContentPane().add(panel);
		getRootPane().setDefaultButton(find);
		
		panel.add(new JLabel("Find"));
		
		text = new JTextField(10);
		text.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				check();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				check();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				check();
			}
			
			private void check() {
				find.setEnabled(text.getDocument().getLength() > 0);
			}
			
		});
		panel.add(text);
		
		casesensitive.setText("Case Sensitive");
		casesensitive.setSelected(false);
		panel.add(casesensitive);
		
		find.setText("Find");
		find.setEnabled(false);
		find.addActionListener(this);
		panel.add(find);
		
		cancel.setText("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
			
		});
		panel.add(cancel);
		
		pack();
		
		setTitle("Find...");
		setSize(width, height);
		setLocationRelativeTo(frame);
	}
	
	public void actionPerformed(ActionEvent e) {
    	setVisible(false);
    	
    	final Clustering clustering = proj.getCluster();
    	final JTree tree = frame.getClusterViewer().getTree();
    	final String string = text.getText();
    	final int position = 0;
    	final boolean casesensitive = this.casesensitive.isSelected();
    	
    	final class Integer {
    		private int value;
    		
    		public Integer(int value) {
    			this.value = value;
    		}
    		
    		public int getValue() {
    			return value;
    		}
    		
    		public void incremement() {
    			value++;
    		}
    		
    		public void decrement() {
    			value--;
    		}
    	}
    	final class Boolean {
    		private boolean value;
    		
    		public Boolean(boolean value) {
    			this.value = value;
    		}
    		
    		public boolean getValue() {
    			return value;
    		}
    		
    		public void setValue(boolean value) {
    			this.value = value;
    		}
    	}
    	
    	final Integer clusterrow = new Integer(-1);
    	final Integer matrixrow = new Integer(-1);
    	final Boolean match = new Boolean(false);
    	
    	// TODO need to describe this algorithm
    	clustering.visit(new ClusterVisitor() {
			private boolean stop = false;
			private boolean collapsed = tree.isCollapsed(0);
			
			@Override
			public void visit(final ClusterSet set)
			{
				if (!match.getValue()) {
					clusterrow.incremement();
					if (tree.isCollapsed(clusterrow.getValue())) {
						collapsed = true;
						matrixrow.incremement();
					}
					analyze(set);
					if (match.getValue() && tree.isCollapsed(clusterrow.getValue()))
						matrixrow.decrement();
					for (Clustering cls : set)
						if (!collapsed)
							cls.visit(this);
					collapsed = false;
				}
				else
					if (!stop) {
						if (tree.isCollapsed(clusterrow.getValue())) {
							collapsed = true;
							matrixrow.incremement();
							stop = true;
						}
						else
							for (Clustering cls : set)
								if (!collapsed)
									cls.visit(this);
					}
			}

			@Override
			public void visit(final ClusterItem item)
			{
				if (!match.getValue()) {
					clusterrow.incremement();
					matrixrow.incremement();
					analyze(item);
					if (match.getValue())
						stop = true;
				}
				else
					if (!stop) {
						matrixrow.incremement();
						stop = true;
					}
			}

			private void analyze(final Clustering cluster) {
				if (clusterrow.getValue() > position && cluster instanceof ClusterItem) {
					if (casesensitive)
						match.setValue(cluster.getName().equals((CharSequence)string));
					else
						match.setValue(cluster.getName().toLowerCase().equals((CharSequence)string.toLowerCase()));
				}
			}
		});
    	
    	if (!match.getValue()) {
    		JOptionPane.showMessageDialog(frame, "\"" + string + "\" not found");
    		return;
    	}
    	
    	frame.getClusterViewer().getTree().scrollRowToVisible(clusterrow.getValue());
    	frame.getClusterViewer().getTree().setSelectionRow(clusterrow.getValue());
    	
    	int leafs = frame.getMatrixViewer().getRows().getSize();
    	int min = frame.getMatrixViewer().getVerticalScrollBar().getMinimum();
    	int max = frame.getMatrixViewer().getVerticalScrollBar().getMaximum();
    	frame.getMatrixViewer().getVerticalScrollBar().setValue((int)(((1.0 * matrixrow.getValue() / leafs) * (max - min)) + min));
    }
	
}
