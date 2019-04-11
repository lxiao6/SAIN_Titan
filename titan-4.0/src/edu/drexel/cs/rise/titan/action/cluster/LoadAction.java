/*
 * LoadAction.java
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
package edu.drexel.cs.rise.titan.action.cluster;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.cluster.FileParser;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.action.FileAction;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class LoadAction extends FileAction
{
	private static final long serialVersionUID = 10L;
	protected final Project proj;
	public LoadAction(final Project proj,final Component parent)
	{
		super(proj,parent);
		this.proj = proj;
		initialize();
	}
	
	private void initialize()
	{
		putValue(NAME, "Load Clustering...");
		putValue(MNEMONIC_KEY, (int)'L');
		putValue(SHORT_DESCRIPTION, "Load Clustering");
		putValue(SMALL_ICON, IconFactory.load("open-clsx.png"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.SHIFT_MASK | ActionUtilities.CMD_KEY));

		final FileFilter filter = new FileNameExtensionFilter(
				"Cluster file (*.clsx)", "clsx");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Open Cluster File");
		chooser.setMultiSelectionEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		//final Project proj = Project.getInstance();
		if (proj.getStructureDependency() == null || !ActionUtilities.querySave(proj))
			return;
		
		prepare();
		if (showOpenDialog() != JFileChooser.CANCEL_OPTION) {
			final File path = chooser.getSelectedFile();
			load(path);
		}
	}

	public void load(final File path)
	{
		//final Project proj = Project.getInstance();

		try {
			final Clustering cls = ClusterUtilities.trim(
					FileParser.load(path), proj.getStructureDependency().vertices());
			proj.clearCollapsed();
			proj.getCollapsed().add(cls);
			proj.setCluster(cls, path);
			proj.setModified(false);
		}
		catch (MinosException ex) {
			JOptionPane.showMessageDialog(parent,
					"Unable to load cluster file: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
