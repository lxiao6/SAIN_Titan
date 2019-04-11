/*
 * ClusterViewer.java
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
package edu.drexel.cs.rise.titan.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ProjectListener;
import edu.drexel.cs.rise.titan.action.cluster.CollapseAllAction;
import edu.drexel.cs.rise.titan.action.cluster.DeleteAction;
import edu.drexel.cs.rise.titan.action.cluster.DownAction;
import edu.drexel.cs.rise.titan.action.cluster.ExpandAllAction;
import edu.drexel.cs.rise.titan.action.cluster.GroupAction;
import edu.drexel.cs.rise.titan.action.cluster.RenameAction;
import edu.drexel.cs.rise.titan.action.cluster.SortAction;
import edu.drexel.cs.rise.titan.action.cluster.UngroupAction;
import edu.drexel.cs.rise.titan.action.cluster.UpAction;
import edu.drexel.cs.rise.titan.action.cluster.split.SplitAction;
import edu.drexel.cs.rise.titan.action.cluster.split.SubSystemAction;
import edu.drexel.cs.rise.titan.model.ClusterModel;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class ClusterViewer extends JPanel
{
    private static final long serialVersionUID = 10L;

    private final Map<String, Action> actions = new HashMap<String, Action>();
    private final ClusterModel model;
    private final JTree tree;
    private JPopupMenu contextMenu;
    protected final Project proj;

    public ClusterViewer(final Project proj)
    {
        this(proj, new ClusterModel());
    }

    public ClusterViewer(final Project proj, final ClusterModel model)
    {
        this.proj = proj;
        this.model = model;
        this.tree = new JTree(model);

        initialize();
    }

    private void initialize()
    {
        if (model == null)
            throw new NullPointerException();

        buildActions();

        setLayout(new BorderLayout());
        add(new JScrollPane(tree), BorderLayout.CENTER);
        add(buildToolbar(), BorderLayout.PAGE_START);

        contextMenu = buildContextMenu();

        // final Project proj = Project.getInstance();
        proj.addProjectListener(new Listener());

        enableDragAndDrop();

        enableAllButtons(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent event)
            {
                enableTreeButtons();
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent event)
            {
                showContextMenu(event);
            }

            @Override
            public void mouseReleased(final MouseEvent event)
            {
                showContextMenu(event);
            }

            private void showContextMenu(final MouseEvent event)
            {
                if (event.isPopupTrigger() && !event.isControlDown()) {
                    final int x = event.getX();
                    final int y = event.getY();
                    final int row = tree.getRowForLocation(x, y);

                    if (row >= 0
                            && tree.getPathForRow(row).getLastPathComponent() instanceof ClusterSet) {
                        tree.setSelectionRow(row);
                        contextMenu.show(tree, x, y);
                    }
                }
            }
        });
    }

    private void buildActions()
    {
        actions.put("expand", new ExpandAllAction(proj, tree));
        actions.put("collapse", new CollapseAllAction(proj, tree));
        actions.put("group", new GroupAction(proj, this));
        actions.put("ungroup", new UngroupAction(proj, this));
        actions.put("up", new UpAction(proj, this));
        actions.put("down", new DownAction(proj, this));
        actions.put("delete", new DeleteAction(proj, this));
        actions.put("rename", new RenameAction(proj, this));
        actions.put("sort", new SortAction(proj, this));
        actions.put("split", new SplitAction(proj, this));
        actions.put("sub", new SubSystemAction(proj, this));
    }

    private JComponent buildToolbar()
    {
        JButton button;

        final JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        button = new JButton(actions.get("expand"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("collapse"));
        button.setText("");
        bar.add(button);

        bar.addSeparator();

        button = new JButton(actions.get("group"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("ungroup"));
        button.setText("");
        bar.add(button);

        bar.addSeparator();

        button = new JButton(actions.get("up"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("down"));
        button.setText("");
        bar.add(button);

        bar.addSeparator();

        button = new JButton(actions.get("delete"));
        button.setText("");
        bar.add(button);

        bar.addSeparator();

        button = new JButton(actions.get("split"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("sub"));
        button.setText("");
        bar.add(button);

        return bar;
    }

    private JPopupMenu buildContextMenu()
    {
        final JPopupMenu menu = new JPopupMenu();

        menu.add(new JMenuItem(actions.get("rename")));
        menu.add(new JMenuItem(actions.get("sort")));

        return menu;
    }

    private void enableDragAndDrop()
    {
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON);
        /*
         * tree.setTransferHandler(new TransferHandler() {
         * private static final long serialVersionUID = 10L;
         * 
         * public boolean canImport(final TransferHandler.TransferSupport info)
         * {
         * // only handle drops, not from clipboard
         * if (!info.isDrop())
         * return false;
         * 
         * final JTree.DropLocation location =
         * (JTree.DropLocation) info.getDropLocation();
         * final TreePath parent = location.getPath();
         * 
         * return parent != null;
         * }
         * 
         * public boolean importData(
         * final TransferHandler.TransferSupport info)
         * {
         * if (!canImport(info))
         * return false;
         * 
         * final JTree.DropLocation location =
         * (JTree.DropLocation) info.getDropLocation();
         * final TreePath parent = location.getPath();
         * 
         * 
         * }
         * });
         */
    }

    public ClusterModel getModel()
    {
        return model;
    }

    public TreeSelectionModel getSelectionModel()
    {
        return tree.getSelectionModel();
    }

    public JTree getTree()
    {
        return tree;
    }

    protected class Listener implements ProjectListener
    {
        @Override
        public void clusterChanged(final Project proj)
        {
            enableAllButtons(proj.getCluster() != null);
            final Clustering root = proj.getCluster();

            // the project's cluster and model's cluster will be the same
            // when the the cluster is set in dependencyChanged
            // and will be different when set in loading from a file
            if (root != model.getRoot())
                model.setRoot(root);

            if (root != null)
                tree.collapsePath(new TreePath(root));
        }

        @Override
        public void structureDependencyChanged(final Project proj)
        {
            final WeightedDigraph<String> dsm = proj.getStructureDependency();
            final Clustering cls = ClusterUtilities.buildDefaultCluster(dsm);
            proj.clearCollapsed();
            if (cls != null)
                proj.getCollapsed().add(cls);

            model.setRoot(cls);
            proj.setCluster(cls);
        }

        /*
         * Lu Xiao:
         * *This is somewhere I'm not so sure of right now.
         * How the change of history Dependency would change the clustering
         * So now just make this exactly the same as structureDependencyChanged.
         * 2013-8-8
         * I decide that the change of history won't change the clustering.
         */
        @Override
        public void historyDependencyChanged(final Project proj)
        {
            /*
             * final WeightedDigraph<String> dsm = proj.getStructureDependency();
             * final Clustering cls = ClusterUtilities.buildDefaultCluster(dsm);
             * proj.clearCollapsed();
             * if (cls != null)
             * proj.getCollapsed().add(cls);
             * 
             * model.setRoot(cls);
             * proj.setCluster(cls);
             */
        }

        @Override
        public void f2fDependencyChanged(final Project proj)
        {

        }

        @Override
        public void modified(final Project proj)
        {

        }
    }

    public void enableAllButtons(boolean b)
    {
        actions.get("expand").setEnabled(b);
        actions.get("collapse").setEnabled(b);

        enableTreeButtons();
    }

    protected void enableTreeButtons()
    {
        final TreeSelectionModel selection = tree.getSelectionModel();

        enableSplitButton(selection);
        enableGroupButtons(selection); /* group and subsystem are enabled all together */
        enabledMoveButtons(selection);
        enableDeleteButton(selection);
    }

    protected void enableSplitButton(final TreeSelectionModel select)
    {
        boolean split = true;
        if (select.isSelectionEmpty()) {
            split = false;
        }
        else {
            final TreePath parentPath = select.getSelectionPath().getParentPath();
            final ClusterSet common = parentPath != null ? (ClusterSet) parentPath
                    .getLastPathComponent() : null;

            if (common == null) {
                split = false;
            }
            else {
                final int[] rows = select.getSelectionRows();
                Arrays.sort(rows);
                for (int i = rows.length - 1; i >= 0; --i) {
                    final TreePath path = tree.getPathForRow(rows[i]);
                    final Clustering cls = (Clustering) path.getLastPathComponent();
                    if (cls instanceof ClusterSet) {
                        split = false;
                        System.out.print("set : ");
                    }
                    if (!(split))
                        break;
                }
            }
        }
        actions.get("split").setEnabled(split);
    }

    protected void enableGroupButtons(final TreeSelectionModel select)
    {
        boolean group_or_sub = true;
        boolean ungroup = true;

        if (select.isSelectionEmpty()) {
            group_or_sub = false;
            ungroup = false;
        }
        else {
            final TreePath parentPath = select.getSelectionPath().getParentPath();
            final ClusterSet common = parentPath != null ? (ClusterSet) parentPath
                    .getLastPathComponent() : null;

            if (common == null) {
                group_or_sub = false;
                ungroup = false;
            }
            else {
                for (TreePath path : select.getSelectionPaths()) {
                    final TreePath parent = path.getParentPath();
                    if (parent == null || parent.getLastPathComponent() != common)
                        group_or_sub = false;

                    if (!(path.getLastPathComponent() instanceof ClusterSet))
                        ungroup = false;

                    if (!(group_or_sub || ungroup))
                        break;
                }
            }
        }

        actions.get("group").setEnabled(group_or_sub);
        actions.get("sub").setEnabled(group_or_sub);
        actions.get("ungroup").setEnabled(ungroup);
    }

    protected void enabledMoveButtons(final TreeSelectionModel select)
    {
        boolean up = true;
        boolean down = true;

        if (select.isSelectionEmpty()) {
            up = false;
            down = false;
        }
        else {
            final TreePath parentPath = select.getSelectionPath().getParentPath();
            final ClusterSet common = parentPath != null ? (ClusterSet) parentPath
                    .getLastPathComponent() : null;

            if (common == null) {
                up = false;
                down = false;
            }
            else {
                for (TreePath path : select.getSelectionPaths()) {
                    final TreePath parent = path.getParentPath();
                    if (parent == null || parent.getLastPathComponent() != common) {
                        up = false;
                        down = false;
                        break;
                    }

                    final Clustering cls = (Clustering) path.getLastPathComponent();
                    final int index = common.getClusterIndex(cls);
                    if (index == 0)
                        up = false;
                    if (index == common.clusters().size() - 1)
                        down = false;

                    if (!(up || down))
                        break;
                }
            }
        }

        actions.get("up").setEnabled(up);
        actions.get("down").setEnabled(down);
    }

    protected void enableDeleteButton(final TreeSelectionModel select)
    {
        final Clustering cls = proj.getCluster();
        final TreePath root = cls == null ? null : new TreePath(cls);
        boolean b = !(select.isSelectionEmpty() || (select.getSelectionCount() == 1 && select
                .getSelectionPath().equals(root)));

        actions.get("delete").setEnabled(b);
    }

    public boolean isExpanded(final TreePath path)
    {
        return tree.isExpanded(path);
    }
}
