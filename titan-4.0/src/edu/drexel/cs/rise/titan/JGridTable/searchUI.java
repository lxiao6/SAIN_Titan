package edu.drexel.cs.rise.titan.JGridTable;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.titan.JGridTable.FoundListUI;

public class searchUI extends JFrame{
	
	//private static final long serialVersionUID = 10L;
	private JTextComponent projectName = null;
	private JTextComponent projectSize = null;
	private Vector<refProject> foundProjects = new Vector<refProject>();
	private repoManager repo;
	static JFrame f = new JFrame("Find Projects");
	
	public searchUI(repoManager repo){
		super();
		this.repo = repo;
	}
	
	public void init(){
		
		this.setLayout(new GridLayout(2, 1));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle      ( "Search Repository");
		
		/*final JTextComponent projectName = new JTextField(100);
		projectName.isEditable();
		this.add(createInputField("Project Name:",100,projectName));
		
		final JTextComponent projectSize = new JTextField(10);
		projectSize.isEditable();
		this.add(createInputField("Project Size:  ",10,projectSize));*/
		
		this.add(inputPanel());
		this.add(buttonPanel());
		
		
		
		this.setSize       ( 400, 200 );
		this.setBackground ( Color.gray );
		this.setVisible (true);
	}
	
	private Component buttonPanel() {
		JPanel buttonP = new JPanel(new BorderLayout());
		
		JButton button = new JButton("Search");
		//button.setSize(10, 10);
		button.setMnemonic('S');
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				String name = projectName.getText();
				String ssize = projectSize.getText();
				int size = 0;
				System.out.println("Search:"+name+","+size+".");
				
				/***********************/
				if(name.isEmpty()) name = null;
				if(ssize.isEmpty())
					size = -1;
				else size = Integer.parseInt(ssize);
				foundProjects.clear();
				foundProjects.addAll(repo.search(name, size));
			    /***********************/
				
				
				//test.main(null);
				
				f.setLayout(new BorderLayout());
			   
				
			  
				f.getContentPane().removeAll();
			    f.getContentPane().add(FoundListUI.getFoundListUIInstance(repo,foundProjects), BorderLayout.CENTER);
			    f.setSize(800, 550);
			    f.setVisible(true);
			    f.addWindowListener(new WindowAdapter() {
			      public void windowClosing(WindowEvent e) {
			        e.getWindow().setVisible(false);
			      }
			    });
			}
		});
		
		//buttonP.add(null);
		//buttonP.add(null);
		buttonP.add(button, BorderLayout.PAGE_END);
		
		return buttonP;
	}

	private Component inputPanel() {
		
		int numPairs = 2;
		JPanel inputP = new JPanel(new SpringLayout());
		
		JLabel nameL = new JLabel("Project Name",JLabel.TRAILING);
		inputP.add(nameL);
		
		projectName = new JTextField(100);
		projectName.isEditable();
		nameL.setLabelFor(projectName);
		inputP.add(projectName);
		
		JLabel sizeL = new JLabel("Project Size",JLabel.TRAILING);
		inputP.add(sizeL);
		
		projectSize = new JTextField(100);
		projectSize.isEditable();
		sizeL.setLabelFor(projectSize);
		inputP.add(projectSize);
		
	

		//Lay out the panel.
		SpringUtilities.makeCompactGrid(inputP,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad
		return inputP;
	}

	

}
