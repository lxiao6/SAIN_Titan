/*
 * GroupAction.java
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
package edu.drexel.cs.rise.titan.action.cluster.split;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.ArchDRH.Split;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.Viewer;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class SplitAction extends AbstractAction
{
    private static final long serialVersionUID = 10L;

    protected final ClusterViewer viewer;
    protected final Project proj;

    public SplitAction(final Project proj, final ClusterViewer viewer)
    {
        this.proj = proj;
        this.viewer = viewer;

        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Split");
        putValue(SHORT_DESCRIPTION, "Split");
        putValue(SMALL_ICON, IconFactory.load("split.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtilities.CMD_KEY));
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {

        ClusterSet splitRules = new ClusterSet("root");
        final TreeSelectionModel select = viewer.getSelectionModel();
        if (select.isSelectionEmpty())
            return;

        final TreePath parentPath = select.getSelectionPath().getParentPath();
        final ClusterSet common = parentPath != null ? (ClusterSet) parentPath
                .getLastPathComponent() : null;

        if (common == null)
            return;

        final JTree tree = viewer.getTree();
        final int[] rows = select.getSelectionRows();
        Arrays.sort(rows);

        /*
         * input for split
         * 1. The spliting rules: read minos.cluster.FileParser
         * 2. The dsm of the project: this.proj
         */

        /*
         * generate the split result, feed to new Viewer and new Project
         * newSplit copy old this.proj
         * newSplit.cluster = split generated clusterings.
         */

        for (int i = rows.length - 1; i >= 0; --i) {
            final TreePath path = tree.getPathForRow(rows[i]);
            final ClusterItem cls = (ClusterItem) path.getLastPathComponent();
            splitRules.addCluster(cls);
            cls.setParent(splitRules);
        }

        final String title = queryName(splitRules);
        if (title == null)
            return;
        else if (!ClusterUtilities.isValidName(title)) {
            JOptionPane.showMessageDialog(null, "Invalid split title.", "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Set<String> rules = new HashSet<String>();
        rules.addAll(splitRules.items());
        // e mySplit = new e(copy(proj.getCluster()), ClusterUtilities.getGraph(proj), rules);
        // mySplit.c();
        // Clustering splitClsx = mySplit.a();

        Split mySplit = new Split(copy(proj.getCluster()), ClusterUtilities.getGraph(proj), rules);
        mySplit.getCluster();
        Clustering splitClsx = mySplit.getCluster();

        Project newProj = new Project(proj);

        newProj.setProjectName(title);
        newProj.setCurName(title);

        if (proj.getStructureDependency() != null) {
            Set<String> temp = new HashSet<String>();
            temp.addAll(splitClsx.items());
            newProj.setStructureDependency(proj.getStructureDependency().subset(temp));
            newProj.setCurSize(newProj.getStructureDependency().vertices().size());
        }
        if (proj.getHistoryDependency() != null) {
            Set<String> temp = new HashSet<String>();
            temp.addAll(splitClsx.items());
            newProj.setHistory_dependency(proj.getHistoryDependency().subset(temp));
        }
        newProj.setCluster(splitClsx);

        Viewer newSplit = new Viewer(newProj);

        // newSplit.update();
        newSplit.updateTitle(title);
        newSplit.getClusterViewer().getModel().setRoot(splitClsx);
        newSplit.setLocationRelativeTo(null);
        newSplit.setVisible(true);
        newSplit.getClusterViewer().enableAllButtons(true);
    }

    private Clustering copy(Clustering cluster)
    {

        Clustering copy = null;
        if (cluster instanceof ClusterItem) {
            copy = new ClusterItem(cluster.getName());
        }
        else {
            copy = new ClusterSet(cluster.getName());
            for (Clustering c : cluster) {
                Clustering cc = copy(c);
                cc.setParent((ClusterSet) copy);
                ((ClusterSet) copy).addCluster(cc);
            }
        }
        return copy;
    }

    protected String queryName(ClusterSet splitRules)
    {
        Vector<String> options = new Vector<String>();
        for (Clustering c : splitRules) {
            options.add(c.getName());
        }
        String option = (String) JOptionPane.showInputDialog(null,
                "Select split title, click cancel to input manually", "Split Title",
                JOptionPane.QUESTION_MESSAGE, IconFactory.load("split.png"), options.toArray(),
                "default");
        if (option == null)
            option = JOptionPane.showInputDialog(null, "Enter split name:", "Split Name",
                    JOptionPane.PLAIN_MESSAGE);
        return option;

    }

}
