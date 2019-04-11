package edu.drexel.cs.rise.titan.JGridTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

class submitListener implements ActionListener{

	  SortableTableModel dd ;
	  int row;
	  
	public submitListener(SortableTableModel dm, int row){
		  this.dd = dm;
		  this.row = row;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
			JRadioButton but = (JRadioButton) this.dd.getValueAt(0, 0);
			but.setText("new");
			System.out.println("change it to new");
		}
		
	}