package edu.drexel.cs.rise.titan.JGridTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.caelus.CaelusException;
import edu.drexel.cs.rise.caelus.SwingUtilities;

public class EditUI extends JFrame{
	
	private static final long serialVersionUID = 10L;
	private refProject projectEdit = null;
	SortableTableModel dm ;
	String projName;
	JTable table;
	private JPanel p1;
	private JPanel p2;
	private JPanel p3;
	private JTextField projectName = null;
	private JTextField projectSize = null;
	private JTextField projectPC = null;
	private JTextField projectLevel = null;
	private JTextField projectDepth = null;
	private JTextField projectSmil = null;
	private JTextField projectGmil = null;

	private JTextComponent archIssueDir = null;
    private JFileChooser dirChooser;
    
    private static EditUI singleton = new EditUI();
    private repoManager repo;
	
    private EditUI(){
    	
    }
    
	private EditUI( SortableTableModel d, String selectedRow, JTable t ){
		super();
		dm = d;
		table = t;
		
		
		
		this.projName = selectedRow;
		this.projectEdit = getSelectedProject();
		
	}
	
	public static EditUI getEditUIInstance(SortableTableModel d, String proj, JTable t, repoManager repo){
		singleton.dm = d;
		singleton.table = t;
		singleton.projName = proj;
		singleton.projectEdit = singleton.getSelectedProject();
		singleton.repo = repo;
		return singleton;
	}
	
	 private int getRow(String cmd) {
			
			int row = dm.getRowCount();
			for(int i = 0; i < row; i++){
				String name = ((JRadioButton)dm.getValueAt(i, 0)).getText();
				if(name.equals(cmd)){
					return i;
				}
			}
			return 0;
		}

	
	private refProject getSelectedProject() {
		
		int row = getRow(projName);
		refProject proj = new refProject();
		JRadioButton but = (JRadioButton) dm.getValueAt(row, 0);
		
		String name = but.getText();
		proj.setName(name);
		
		int size = (int) dm.getValueAt(row, 1);
		proj.setSize(size);
		
		double pc = (double) dm.getValueAt(row, 2);
		proj.setPc(pc);
		
		int level = (int) dm.getValueAt(row, 3);
		proj.setArchLevel(level);
		
		int depth = (int) dm.getValueAt(row, 4);
		proj.setArchDepth(depth);
		
		double smil = (double) dm.getValueAt(row, 5);
		proj.setsMil(smil);
		
		double gmil = (double) dm.getValueAt(row, 6);
		proj.setgMil(gmil);
		
		String dir = (String) dm.getValueAt(row, 7);
		proj.setArchIssueDir(new File(dir));
		
		return proj;
	}
	
	

	public void init(){
		if(p1 != null)
			this.remove(p1);
		if(p2 != null)
			this.remove(p2);
		if(p3 != null)
			this.remove(p3);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String title = "Edit Project - "+projectEdit.getName();
		this.setTitle      ( title);
		
		p1 = contentPanel();
		p2 = addSpecifyDirPanel();
		p3 = controlPanel();
		this.add(p1, BorderLayout.PAGE_START);	
		this.add(p2, BorderLayout.CENTER);
		this.add(p3, BorderLayout.PAGE_END);
		
		
		
		this.setSize       ( 450, 400 );
		this.setBackground ( Color.gray );
		this.setVisible (true);
	}
	
	private void initDirChooser(){
		
        dirChooser = new JFileChooser("ArchIssue Directory");
        dirChooser.setSelectedFile(projectEdit.getArchIssueDir());
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.addChoosableFileFilter(dirChooser
                .getAcceptAllFileFilter());
       
    }
	private JPanel addSpecifyDirPanel() {

		
        initDirChooser();

		final JButton button = new JButton("...");
		button.setMaximumSize(button.getPreferredSize());
		button.setToolTipText("Select Arch Issue Directory");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				dirChooser.setCurrentDirectory(projectEdit.getArchIssueDir());
				//ruleChooser.setSelectedFile(inRules);

				final int reply = dirChooser.showDialog(EditUI.this, "Select");
				if (reply == JFileChooser.APPROVE_OPTION) {
					try {
						setArchIssueDirectory(dirChooser.getSelectedFile());
					}
					catch (IOException ex) {
						JOptionPane.showMessageDialog(EditUI.this,
								ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		archIssueDir = new JTextField(100);
		archIssueDir.setText(projectEdit.getArchIssueDir().getAbsolutePath());
		archIssueDir.isEnabled();
		archIssueDir.isVisible();
		final JPanel panel = createFileSelector(archIssueDir, button);
		panel.setBorder(BorderFactory.createTitledBorder("Specify ArchIssue Directory"));
		return panel;
	}
	
	private JPanel createFileSelector(final JTextComponent field,
			final JButton button)
	{
		//final SpringLayout layout = new SpringLayout();
		final JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 20;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(field,c);
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(button,c);
		return panel;
	}
	
	protected final void setArchIssueDirectory(final File file) throws IOException
	{
		try {
			SwingUtilities.dispatchAndWait(new Runnable() {
				@Override
				public void run()
				{
					archIssueDir.setText(file.getAbsolutePath());
				}
			});
		}
		catch (CaelusException ex) {
			System.err.println("Unable to update input file field.");
			
		}
	}	
	
private JPanel controlPanel() {
	
	
	
		JPanel controlPanel = new JPanel();
		JButton but = new JButton("Submit" );
		
		but.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				String orig_name = FoundListUI.getSelectedRow();
				System.out.println("Submit clicked, selectedRow "+orig_name);
				int row = getRow(projName);
				
				String new_name = projectName.getText();
				int size = Integer.parseInt(projectSize.getText());
				double pc = Double.parseDouble(projectPC.getText());
				int level = Integer.parseInt(projectLevel.getText());
				int depth = Integer.parseInt(projectDepth.getText());
				double smil = Double.parseDouble(projectSmil.getText());
				double gmil = Double.parseDouble(projectGmil.getText());
				String dir = dirChooser.getSelectedFile().getAbsolutePath();
				
				
				if(!new_name.equals(orig_name)){
					JRadioButton but = (JRadioButton) dm.getValueAt(row, 0);
					but.setText(new_name);
					but.setActionCommand(new_name);
					FoundListUI.setSelected(new_name);
					projectEdit.setName(new_name);
					projName = new_name;
				}
				if(projectEdit.getSize() != size){
					dm.setValueAt(size, row, 1);
					projectEdit.setSize(size);
				}
				if(projectEdit.getPc() != pc){
					dm.setValueAt(new Double(pc), row, 2);
					projectEdit.setPc(pc);
				}
				if(projectEdit.getArchLevel() != level){
					dm.setValueAt(level, row, 3);
					projectEdit.setArchLevel(level);
				}
				if(projectEdit.getArchDepth() != depth){
					dm.setValueAt(depth, row, 4);
					projectEdit.setArchDepth(depth);
				}
				if(projectEdit.getsMil() != smil){
					dm.setValueAt(smil, row, 5);
					projectEdit.setsMil(smil);
				}
				if(projectEdit.getgMil() != gmil){
					dm.setValueAt(gmil, row, 6);
					projectEdit.setgMil(gmil);
				}
				if(!projectEdit.getArchIssueDir().getAbsolutePath().equals(dir)){
					dm.setValueAt(dir, row, 7);
					projectEdit.setArchIssueDir(new File(dir));
				}
				
				repo.saveChange(orig_name, projectEdit);
				
				JOptionPane.showMessageDialog(null,
					    "New Project "+projectEdit.getName()+" is saved to index.");
				
				System.out.println("Currently rank by "+FoundListUI.sortByCol+" asd "+FoundListUI.isAsd);
				dm.sortByColumn(FoundListUI.sortByCol, FoundListUI.isAsd);
				table.repaint();
				System.out.println(this.getClass());
				
				
				
			}
		});
		//but.addActionListener(new submitListener(dm,row));
		
		controlPanel.add(but);
		
		return controlPanel;
	}

private JPanel contentPanel() {
		
		int numPairs = 7;
		JPanel inputP = new JPanel(new SpringLayout());
		
		//Project Name
		JLabel nameL = new JLabel("Project Name",JLabel.TRAILING);
		inputP.add(nameL);		
		projectName = new JTextField(100);
		projectName.setEditable(true);
		projectName.setText(projectEdit.getName());
		nameL.setLabelFor(projectName);
		inputP.add(projectName);
		
		//Project Size
		JLabel sizeL = new JLabel("Project Size",JLabel.TRAILING);
		inputP.add(sizeL);		
		projectSize = new JTextField(100);
		projectSize.setEditable(true);
		projectSize.setText(Integer.toString(projectEdit.getSize()));
		sizeL.setLabelFor(projectSize);
		inputP.add(projectSize);
		
		//pc
		String pc = Double.toString(projectEdit.getPc());
		JLabel pcL = new JLabel("Project PC",JLabel.TRAILING);
		inputP.add(pcL);		
		projectPC = new JTextField(100);
		projectPC.setEditable(true);
		projectPC.setText(pc);
		pcL.setLabelFor(projectPC);
		inputP.add(projectPC);
				
		//archLevel
		String level = Integer.toString(projectEdit.getArchLevel());
		JLabel levelL = new JLabel("Arch Level",JLabel.TRAILING);
		inputP.add(levelL);		
		projectLevel = new JTextField(100);
		projectLevel.setEditable(true);
		projectLevel.setText(level);
		levelL.setLabelFor(projectLevel);
		inputP.add(projectLevel);
				
		//archDepth
		String depth = Integer.toString(projectEdit.getArchDepth());
		JLabel depthL = new JLabel("Arch Depth",JLabel.TRAILING);
		inputP.add(depthL);		
		projectDepth = new JTextField(100);
		projectDepth.setEditable(true);
		projectDepth.setText(depth);
		depthL.setLabelFor(projectDepth);
		inputP.add(projectDepth);
		
		//smil
		String smil = Double.toString(projectEdit.getsMil());
		JLabel smilL = new JLabel("Strict Independent Level",JLabel.TRAILING);
		inputP.add(smilL);		
		projectSmil = new JTextField(100);
		projectSmil.setEditable(true);
		projectSmil.setText(smil);
		smilL.setLabelFor(projectSmil);
		inputP.add(projectSmil);
		
		//gmil
		String gmil = Double.toString(projectEdit.getgMil());
		JLabel gmilL = new JLabel("General Independent Level",JLabel.TRAILING);
		inputP.add(gmilL);		
		projectGmil = new JTextField(100);
		projectGmil.setEditable(true);
		projectGmil.setText(gmil);
		gmilL.setLabelFor(projectGmil);
		inputP.add(projectGmil);

		//Lay out the panel.
		SpringUtilities.makeCompactGrid(inputP,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad
		return inputP;
	}

	

  
	  
	

	
}
