package edu.drexel.cs.rise.titan.JGridTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.caelus.CaelusException;
import edu.drexel.cs.rise.caelus.SwingUtilities;

public class AddUI extends JFrame{
	
	protected static final long serialVersionUID = 10L;
	protected refProject projAdd = null;
	
	protected JTextField projectName = null;
	protected JTextField projectSize = null;
	protected JTextField projectPC = null;
	protected JTextField projectLevel = null;
	protected JTextField projectDepth = null;
	protected JTextField projectSmil = null;
	protected JTextField projectGmil = null;

	protected JTextComponent archIssueDir = null;
	protected JFileChooser dirChooser;
    
	protected final File curDir = new File("./");
	protected repoManager repo;
	private refProject projBeforeEdit;
	boolean current = false;
	
	public AddUI(repoManager repo){
		super();
		this.repo = repo;
	}
	
	public void init(){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String title = "Add New Project";
		this.setTitle      ( title);
		
	
		this.add(contentPanel(), BorderLayout.PAGE_START);	
		this.add(addSpecifyDirPanel(), BorderLayout.CENTER);
		this.add(controlPanel(), BorderLayout.PAGE_END);
		
		
		
		this.setSize       ( 400, 400 );
		this.setBackground ( Color.gray );
		this.setVisible (true);
	}
	
	private void initDirChooser(){
		
        dirChooser = new JFileChooser("ArchIssue Directory");
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
				dirChooser.setCurrentDirectory(curDir);
				//ruleChooser.setSelectedFile(inRules);

				final int reply = dirChooser.showDialog(AddUI.this, "Select");
				if (reply == JFileChooser.APPROVE_OPTION) {
					try {
						setArchIssueDir(dirChooser.getSelectedFile());
					}
					catch (IOException ex) {
						JOptionPane.showMessageDialog(AddUI.this,
								ex.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		archIssueDir = new JTextField(100);
		archIssueDir.setText(curDir.getAbsolutePath());
		final JPanel panel = createFileSelector(archIssueDir, button);
		panel.setBorder(BorderFactory.createTitledBorder("Specify ArchIssue Directory"));
		return panel;
	}
	
	private JPanel createFileSelector(final JTextComponent field,
			final JButton button)
	{
		final SpringLayout layout = new SpringLayout();
		final JPanel panel = new JPanel(layout);

		field.setEditable(true);
		panel.add(field);
		panel.add(button);

		layout.putConstraint(SpringLayout.NORTH, field, 4,
				SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.SOUTH, panel, 4,
				SpringLayout.SOUTH, field);
		layout.putConstraint(SpringLayout.WEST, field, 4,
				SpringLayout.WEST, panel);

		layout.putConstraint(SpringLayout.NORTH, button, 4,
				SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.SOUTH, panel, 4,
				SpringLayout.SOUTH, button);
		layout.putConstraint(SpringLayout.EAST, panel, 4,
				SpringLayout.EAST, button);

		layout.putConstraint(SpringLayout.WEST, button, 8,
				SpringLayout.EAST, field);

		return panel;
	}
	
	protected final void setArchIssueDir(final File file) throws IOException
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
	
private Component controlPanel() {
		JPanel controlPanel = new JPanel();
		JButton but = new JButton("Add Project" );
		
		but.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				
				
				String name = projectName.getText();
				String size = projectSize.getText();
				String pc = projectPC.getText();
				String level = projectLevel.getText();
				String depth = projectDepth.getText();
				String smil = projectSmil.getText();
				String gmil = projectGmil.getText();
				File dir = curDir;
				if(dirChooser.getSelectedFile() != null)
					dir = dirChooser.getSelectedFile();
				
				if(name.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "Name is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
				}else if(size.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "Size is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else if(pc.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "Pc is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else if(level.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "ArchLevel is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else if(depth.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "ArchDepth is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else if(smil.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "Strict Independent Level is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else if(gmil.isEmpty()){
					JOptionPane.showMessageDialog(null,
						    "General Independent Level is empty",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					
				}else{
					projAdd = new refProject();
					projAdd.setName(name);
					projAdd.setSize(Integer.parseInt(size));
					projAdd.setPc(Double.parseDouble(pc));
					projAdd.setArchLevel(Integer.parseInt(level));
					projAdd.setArchDepth(Integer.parseInt(depth));
					projAdd.setsMil(Double.parseDouble(smil));
					projAdd.setgMil(Double.parseDouble(gmil));
					projAdd.setArchIssueDir(dir);
					
					repo.addRefProj(projAdd);
					repo.saveIndexFile();
					
					JOptionPane.showMessageDialog(null,
						    "New Project "+projAdd.getName()+" is saved to index.");
				}
				
				
				
				
			}
		});
		
		controlPanel.add(but);
		
		return controlPanel;
	}

private Component contentPanel() {
		
		int numPairs = 7;
		JPanel inputP = new JPanel(new SpringLayout());
		
		//Project Name
		JLabel nameL = new JLabel("Project Name",JLabel.TRAILING);
		inputP.add(nameL);		
		projectName = new JTextField(100);
		projectName.setEditable(true);
		if(current) projectName.setText(projBeforeEdit.getName());
		nameL.setLabelFor(projectName);
		inputP.add(projectName);
		
		//Project Size
		JLabel sizeL = new JLabel("Project Size",JLabel.TRAILING);
		inputP.add(sizeL);		
		projectSize = new JTextField(100);
		projectSize.setEditable(true);
		if(current) projectSize.setText(Integer.toString(projBeforeEdit.getSize()));
		sizeL.setLabelFor(projectSize);
		inputP.add(projectSize);
		
		//pc
		
		JLabel pcL = new JLabel("Project PC",JLabel.TRAILING);
		inputP.add(pcL);		
		projectPC = new JTextField(100);
		projectPC.setEditable(true);
		if(current){String pc = Double.toString(projBeforeEdit.getPc());
		projectPC.setText(pc);}
		pcL.setLabelFor(projectPC);
		inputP.add(projectPC);
				
		//archLevel
		
		JLabel levelL = new JLabel("Arch Level",JLabel.TRAILING);
		inputP.add(levelL);		
		projectLevel = new JTextField(100);
		projectLevel.setEditable(true);
		if(current){String level = Integer.toString(projBeforeEdit.getArchLevel());
		projectLevel.setText(level);}
		levelL.setLabelFor(projectLevel);
		inputP.add(projectLevel);
				
		//archDepth
		
		JLabel depthL = new JLabel("Arch Depth",JLabel.TRAILING);
		inputP.add(depthL);		
		projectDepth = new JTextField(100);
		projectDepth.setEditable(true);
		if(current){String depth = Integer.toString(projBeforeEdit.getArchDepth());
		projectDepth.setText(depth);}
		depthL.setLabelFor(projectDepth);
		inputP.add(projectDepth);
		
		//smil
		
		JLabel smilL = new JLabel("Strict Independent Level",JLabel.TRAILING);
		inputP.add(smilL);		
		projectSmil = new JTextField(100);
		projectSmil.setEditable(true);
		if(current){
		String smil = Double.toString(projBeforeEdit.getsMil());
		projectSmil.setText(smil);}
		smilL.setLabelFor(projectSmil);
		inputP.add(projectSmil);
		
		//gmil
		
		JLabel gmilL = new JLabel("General Independent Level",JLabel.TRAILING);
		inputP.add(gmilL);		
		projectGmil = new JTextField(100);
		projectGmil.setEditable(true);
		
		if(current){
		String gmil = Double.toString(projBeforeEdit.getgMil());
		projectGmil.setText(gmil);}
		
		gmilL.setLabelFor(projectGmil);
		inputP.add(projectGmil);

		//Lay out the panel.
		SpringUtilities.makeCompactGrid(inputP,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad
		return inputP;
	}

public void setExistProj(refProject proj) {
	
	this.projBeforeEdit = proj;
	current = true;
	
}

}


