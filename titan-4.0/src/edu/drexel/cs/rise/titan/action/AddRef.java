/*
 * AboutAction.java
 * Copyright (c) 2009, Drexel University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Drexel University nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.titan.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.drexel.cs.rise.DesignSpace.data.refProject;
import edu.drexel.cs.rise.titan.Viewer;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class AddRef extends AbstractAction
{
	private static final long serialVersionUID = 10L;
	
	private Map<String, refProject> refInfo;
	protected final Component parent;

	public AddRef(final Component parent, Map<String, refProject> refInfo)
	{
		this.parent = parent;
		this.refInfo = refInfo;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Add Reference Project");
		//putValue(MNEMONIC_KEY, (int)'A');
		//putValue(SHORT_DESCRIPTION, "About");
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		
		String v = JOptionPane.showInputDialog("Add new reference project in following format: \n" +
				"Name(String), Number of Files(int), pc(double), archLevel(int), archDepth(int), strictMim(double), generalMim(double)\n" +
				"e.g. cassandra-1.2.1,1200,0.45,20,25,0.5,0.6");
		String[] fields = v.split(",");
		if(fields.length < 7){
			JOptionPane.showMessageDialog(null,
				    "New reference project was not added: format error.",
				    "Format error",
				    JOptionPane.ERROR_MESSAGE);
		}else{
			
			String name = fields[0];
			if(!refInfo.containsKey(name)){
				int size = 0;
				double pc = 0;
				int archLevel = 0;
				int archDepth = 0;
				double sMim = 0, gMim = 0;
				try{
					size = Integer.parseInt(fields[1]);
					pc = Double.parseDouble(fields[2]);
					archLevel = Integer.parseInt(fields[3]);
					archDepth = Integer.parseInt(fields[4]);
					sMim = Double.parseDouble(fields[5]);
					gMim = Double.parseDouble(fields[6]);
					refInfo.put(name, new refProject(name,size,pc,archLevel,archDepth,sMim,gMim));
					
					concateToFile(v+"\n");
				}catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(null,
						    "New reference project was not added: format error.",
						    "Format error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}else{
				JOptionPane.showMessageDialog(null,
					    "The same project has already been added.",
					    "Duplicate Item",
					    JOptionPane.ERROR_MESSAGE);
			}
			
			
		}
	}

	private void concateToFile(String data) {
		boolean tag = false;
		try{
    		
    		File file =new File("data.csv");
 
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    			tag = true;
    		}
 
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    	    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	    if(tag){
    	    	bufferWritter.write("Project Name, Number of Files, PC, archLevel, archDepth, strict Mim, general Mim\n");
    	    }
    	    bufferWritter.write(data);
    	    bufferWritter.close();
 
    	    JOptionPane.showMessageDialog(null, "New reference project add to file: data.csv");
 
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
	
}
