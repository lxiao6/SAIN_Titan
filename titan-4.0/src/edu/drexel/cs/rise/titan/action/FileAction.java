/*
 * FileAction.java
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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.drexel.cs.rise.titan.Project;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public abstract class FileAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	protected final Project proj;

	protected final JFileChooser chooser;
	protected final Component parent;
	private int option;

	protected FileAction(final Project proj,final Component parent)
	{
		this.proj = proj;
		this.chooser = new JFileChooser();
		this.parent = parent;
	}

	protected void prepare()
	{
		//final Project proj = Project.getInstance();
		chooser.setCurrentDirectory(proj.getCurrentDirectory());
		option = JFileChooser.CANCEL_OPTION;
	}

	protected final int getOption()
	{
		return option;
	}

	protected final int showOpenDialog()
	{
		option = chooser.showOpenDialog(parent);
		updateCurrentDirectory();
		return option;
	}

	protected final int showSaveDialog()
	{
		option = chooser.showSaveDialog(parent);
		updateCurrentDirectory();
		return option;
	}
	
	protected final void updateCurrentDirectory()
	{
		if (option != JFileChooser.CANCEL_OPTION)
			proj.setCurrentDirectory(
					chooser.getCurrentDirectory());
	}

	protected final File getPathWithExtension(final File path,
			final String extension)
	{
		if (!path.getName().endsWith(extension))
			return new File(path.getParentFile(),
					path.getName() + extension);
		else
			return path;
	}

	protected final boolean confirmOverwrite(final File path)
	{
		return !(path.exists() &&
				queryOverwrite(path.getName()) == JOptionPane.CANCEL_OPTION);
	}

	protected final int queryOverwrite(final String path)
	{
		final String message = String.format(
				"File \"%s\" already exists. Do you want to replace it?",
				path);
		return JOptionPane.showConfirmDialog(parent, message,
				"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
	}
}
