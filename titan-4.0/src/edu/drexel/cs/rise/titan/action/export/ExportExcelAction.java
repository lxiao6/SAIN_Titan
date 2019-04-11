/*
 * ExportAction.java
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
package edu.drexel.cs.rise.titan.action.export;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.civitas.spreadsheet.Excel_v5;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.action.FileAction;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;
import edu.drexel.cs.rise.util.Digraph;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class ExportExcelAction extends FileAction
{
	private static final long serialVersionUID = 10L;
	protected final Project proj;

	public ExportExcelAction(final Project proj,final Component parent)
	{	
		super(proj,parent);
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Excel...");
		putValue(MNEMONIC_KEY, (int) 'x');
		putValue(SHORT_DESCRIPTION, "Export Excel");
		putValue(SMALL_ICON, IconFactory.load("export.png"));

	
		final FileFilter filterXLSX = new FileNameExtensionFilter(
				"Microsoft Excel 2007 Workbook (*.xlsx)", "."
						+ "xlsx");

		
		chooser.addChoosableFileFilter(filterXLSX);
		chooser.setDialogTitle("Export Excel File");
		chooser.setMultiSelectionEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
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

	private void export(final File path)
	{
		final Clustering clsx = proj.getCluster();
		final WeightedDigraph<String> sdsm = ClusterUtilities.getStructureGraph(proj);
		final WeightedDigraph<String> hdsm = ClusterUtilities.getHistoryGraph(proj);
		final Set<Clustering> collapsed = proj.getCollapsed();
		
		//WeightedDigraph<String> translated_sdsm = proj.getDPControl().translate(proj.isWeighted(),sdsm);
		

		// HACK: getting a strange error when trying to run Excel.save,
		// that is why i had to create seperate methods for both types
		
		/*ToDo: Now I don't want to change civitas*/
		Excel_v5 myExcel ;
		if(hdsm.size() == 0){
			myExcel = new Excel_v5(path.getPath(), sdsm, null, clsx, collapsed, proj.getDpTypes());
		}else{
			System.out.println("with history");
			myExcel = new Excel_v5(path.getPath(), sdsm, hdsm, clsx, collapsed, proj.getDpTypes());
		}
		Thread td = new Thread(myExcel);
		td.start();
	}
}
