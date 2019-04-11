/*
 * OpenMatrixAction.java
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
package edu.drexel.cs.rise.titan.action.open;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.DesignSpace.Utilities.ComplexityChecker;
import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.dsm.BiWeightedFileParser;
import edu.drexel.cs.rise.minos.dsm.WeightedFileParser;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.titan.action.FileAction;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class OpenStructureMatrixAction extends FileAction
{
    private static final long serialVersionUID = 10L;
    protected final Project proj;
    protected Viewer parent;
    private Runtime runtime = Runtime.getRuntime();

    public OpenStructureMatrixAction(final Project proj, Viewer parent)
    {
        super(proj, parent);
        this.proj = proj;
        this.parent = parent;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Open Structure DSM...");
        putValue(MNEMONIC_KEY, (int) 'O');
        putValue(SHORT_DESCRIPTION, "Open Structure DSM");
        putValue(SMALL_ICON, IconFactory.load("open-dsm.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionUtilities.CMD_KEY));

        final FileFilter filter = new FileNameExtensionFilter("DSM file (*.dsm)", "dsm");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Open Structure DSM File");
        chooser.setMultiSelectionEnabled(false);
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {
        if (proj.getStructureDependency() != null && !ActionUtilities.querySave(proj))
            return;

        prepare();
        if (showOpenDialog() != JFileChooser.CANCEL_OPTION) {
            final File path = chooser.getSelectedFile();
            load(path);
        }
        parent.updateTitle(proj.getProjectName());
    }

    public void load(final File path)
    {
        try {
            long t1 = ComplexityChecker.getTime("Start load structure dsm:");
            ComplexityChecker.getMemoryUsage(runtime, "Start load structure dsm:");
            final WeightedDigraph<String> dsm = (WeightedDigraph<String>) BiWeightedFileParser
                    .load(path);

            proj.initDP(BiWeightedFileParser.dpTypes);

            if (!proj.getDPControl().dsmFormatCheck(dsm)) {
                throw new MinosException(
                        "The dsm should be in binary code format following the DependencyConfig");
            }

            ComplexityChecker.getMemoryUsage(runtime, "End load structure dsm:");
            long t2 = ComplexityChecker.getTime("End load structure dsm:");
            System.out.println("Loading time is " + (t2 - t1) / 1000);

            parent.update();

            proj.setStructureDependency(dsm);
            proj.setClusterPath(null);
            proj.setModified(false);

            String name = path.getName();
            int index = name.lastIndexOf(".dsm");
            name = name.substring(0, index);
            proj.setProjectName(name);
            proj.setCurName(name);
            proj.setCurSize(dsm.vertices().size());

            File hpath = new File(path.getParentFile(), name + "-history.dsm");
            if (hpath.exists()) {

                final WeightedDigraph<String> temp_hdsm = (WeightedDigraph<String>) WeightedFileParser
                        .load(hpath);
                final WeightedDigraph<String> hdsm = temp_hdsm.subset(dsm.vertices());
                proj.setHistory_dependency(hdsm);

            }
            else {
                proj.setHistory_dependency(null);
                warning(hpath);
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    parent,
                    "Unable to load DSM file, please check the format of your dsm\n "
                            + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    protected void warning(File f)
    {
        JOptionPane.showMessageDialog(null, "Can't find matching history dsm file: " + f.getName()
                + " \nin directory: " + f.getParentFile().getAbsolutePath()
                + "\nPlease use the load history button to load history dsm seperately.",
                "No Match History", JOptionPane.WARNING_MESSAGE);
    }
}
