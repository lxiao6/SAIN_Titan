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
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.drexel.cs.rise.titan.Viewer;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class AboutAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;
	
	private static final String message =
			"Titan%nversion %s%nCopyright(c) 2009-%s, Drexel University";
	protected final Component parent;

	public AboutAction(final Component parent)
	{
		this.parent = parent;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "About...");
		putValue(MNEMONIC_KEY, (int)'A');
		putValue(SHORT_DESCRIPTION, "About");
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		final String year = String.valueOf(Calendar.getInstance().get(
				Calendar.YEAR));
		JOptionPane.showMessageDialog(parent,
				String.format(message, Viewer.version, year),
				"About Titan", JOptionPane.INFORMATION_MESSAGE);
	}
}
