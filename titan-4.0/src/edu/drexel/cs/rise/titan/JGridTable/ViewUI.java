package edu.drexel.cs.rise.titan.JGridTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.drexel.cs.rise.DesignSpace.data.refProject;

public class ViewUI extends JFrame{
	
	private static final long serialVersionUID = 10L;
	private refProject projInfo = null;
	
	public ViewUI(refProject proj){
		super();
		this.projInfo = proj;
	}
	
	public void init(){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String title = "View Project - "+projInfo.getName();
		this.setTitle      ( title);
		
	
		
		this.add(contentPanel(), BorderLayout.CENTER);
		this.add(linkPanel(), BorderLayout.PAGE_END);
		
		
		
		this.setSize       ( 400, 400 );
		this.setBackground ( Color.gray );
		this.setVisible (true);
	}
	
private Component linkPanel() {
		JPanel linkPanel = new JPanel();
		JButton but = new JButton("Click me to open ArchIssues directory..." );
		
		but.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				Desktop desktop = Desktop.getDesktop();
        		try {
        			desktop.open(projInfo.getArchIssueDir());
        		} catch (IOException ex) {
        			ex.printStackTrace();
        		}
			}
		});
		
		linkPanel.add(but);
		
		return linkPanel;
	}

private Component contentPanel() {
		
		int numPairs = 7;
		JPanel inputP = new JPanel(new SpringLayout());
		
		//Project Name
		JLabel nameL = new JLabel("Project Name",JLabel.TRAILING);
		inputP.add(nameL);		
		JTextField projectName = new JTextField(100);
		projectName.setEditable(false);
		projectName.setText(projInfo.getName());
		nameL.setLabelFor(projectName);
		inputP.add(projectName);
		
		//Project Size
		JLabel sizeL = new JLabel("Project Size",JLabel.TRAILING);
		inputP.add(sizeL);		
		JTextField projectSize = new JTextField(100);
		projectSize.setEditable(false);
		projectSize.setText(Integer.toString(projInfo.getSize()));
		sizeL.setLabelFor(projectSize);
		inputP.add(projectSize);
		
		//pc
		String pc = Double.toString(projInfo.getPc());
		JLabel pcL = new JLabel("Project PC",JLabel.TRAILING);
		inputP.add(pcL);		
		JTextField projectPC = new JTextField(100);
		projectPC.setEditable(false);
		projectPC.setText(pc);
		pcL.setLabelFor(projectPC);
		inputP.add(projectPC);
				
		//archLevel
		String level = Integer.toString(projInfo.getArchLevel());
		JLabel levelL = new JLabel("Arch Level",JLabel.TRAILING);
		inputP.add(levelL);		
		JTextField projectLevel = new JTextField(100);
		projectLevel.setEditable(false);
		projectLevel.setText(level);
		levelL.setLabelFor(projectLevel);
		inputP.add(projectLevel);
				
		//archDepth
		String depth = Integer.toString(projInfo.getArchDepth());
		JLabel depthL = new JLabel("Arch Depth",JLabel.TRAILING);
		inputP.add(depthL);		
		JTextField projectDepth = new JTextField(100);
		projectDepth.setEditable(false);
		projectDepth.setText(depth);
		depthL.setLabelFor(projectDepth);
		inputP.add(projectDepth);
		
		//smil
		String smil = Double.toString(projInfo.getsMil());
		JLabel smilL = new JLabel("Strict Independent Level",JLabel.TRAILING);
		inputP.add(smilL);		
		JTextField projectSmil = new JTextField(100);
		projectSmil.setEditable(false);
		projectSmil.setText(smil);
		smilL.setLabelFor(projectSmil);
		inputP.add(projectSmil);
		
		//gmil
		String gmil = Double.toString(projInfo.getgMil());
		JLabel gmilL = new JLabel("General Independent Level",JLabel.TRAILING);
		inputP.add(gmilL);		
		JTextField projectGmil = new JTextField(100);
		projectGmil.setEditable(false);
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
