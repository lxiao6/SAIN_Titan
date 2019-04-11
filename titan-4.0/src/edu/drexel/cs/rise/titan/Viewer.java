/*
 * Viewer.java
 * Copyright (c) 2009, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.titan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import edu.drexel.cs.rise.titan.action.AboutAction;
import edu.drexel.cs.rise.titan.action.AddRef;
import edu.drexel.cs.rise.titan.action.FindAction;
import edu.drexel.cs.rise.titan.action.QuickDrawAction;
import edu.drexel.cs.rise.titan.action.RedrawAction;
import edu.drexel.cs.rise.titan.action.RowLabelAction;
import edu.drexel.cs.rise.titan.action.analyze.ArchIssuesAction;
import edu.drexel.cs.rise.titan.action.analyze.ModuleIndependentLevelAction;
import edu.drexel.cs.rise.titan.action.analyze.PropagationCostAction;
import edu.drexel.cs.rise.titan.action.analyze.setTheCochange;
import edu.drexel.cs.rise.titan.action.analyze.setTheScope;
import edu.drexel.cs.rise.titan.action.analyze.setTheTop;
import edu.drexel.cs.rise.titan.action.cluster.LoadAction;
import edu.drexel.cs.rise.titan.action.cluster.NewAction;
import edu.drexel.cs.rise.titan.action.cluster.SaveAction;
import edu.drexel.cs.rise.titan.action.cluster.SaveAsAction;
import edu.drexel.cs.rise.titan.action.cluster.generate.ArchDrhClusterAction;
import edu.drexel.cs.rise.titan.action.cluster.generate.PkgClusterAction;
import edu.drexel.cs.rise.titan.action.export.ExportExcelAction;
import edu.drexel.cs.rise.titan.action.export.ExportHistoryMatrixAction;
import edu.drexel.cs.rise.titan.action.export.ExportMatrixView2Image;
import edu.drexel.cs.rise.titan.action.export.ExportUnweightedStructureMatrixAction;
import edu.drexel.cs.rise.titan.action.export.ExportWeightedStructureMatrixAction;
import edu.drexel.cs.rise.titan.action.open.OpenHistoryMatrixAction;
import edu.drexel.cs.rise.titan.action.open.OpenStructureMatrixAction;
import edu.drexel.cs.rise.titan.action.open.Openf2fMatrixAction;
import edu.drexel.cs.rise.titan.action.repository.AddExsitProjectAction;
import edu.drexel.cs.rise.titan.action.repository.AddProjectAction;
import edu.drexel.cs.rise.titan.action.repository.SearchProjectAction;
import edu.drexel.cs.rise.titan.action.show.ChangeHistoryDsmThresholdAction;
import edu.drexel.cs.rise.titan.action.show.Changef2fDsmThresholdAction;
import edu.drexel.cs.rise.titan.action.show.DependencyStrengthAction;
import edu.drexel.cs.rise.titan.action.show.ShowDpTypesAction;
import edu.drexel.cs.rise.titan.action.show.ShowHistoryAction;
import edu.drexel.cs.rise.titan.action.show.Showf2fAction;
import edu.drexel.cs.rise.titan.model.ClusterModel;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.ui.MatrixViewer;
import edu.drexel.cs.rise.titan.util.ActionUtilities;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class Viewer extends JFrame
{
    private static final long serialVersionUID = 10L;

    public static final String version = "4.0";

    public Project proj = new Project();
    private final Map<String, Action> actions = new HashMap<String, Action>();
    private Map<String, Action> dpactions = new HashMap<String, Action>();
    protected ClusterViewer cluster;
    protected MatrixViewer matrix;
    public JComponent toolBar;

    public Viewer()
    {
        super("Titan");
        initialize();
    }

    public Viewer(final Project proj)
    {
        super("Titan");
        this.proj = proj;
        initialize();
    }

    public ClusterViewer getClusterViewer()
    {
        return cluster;
    }

    public MatrixViewer getMatrixViewer()
    {
        return matrix;
    }

    public void initialize()
    {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event)
            {
                queryClose();
            }
        });

        setLayout(new BorderLayout());

        cluster = createClusterViewer();
        matrix = createMatrixViewer();

        buildActions();
        setJMenuBar(buildMenus());

        final JSplitPane body = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cluster, matrix);
        add(body, BorderLayout.CENTER);
        body.setOneTouchExpandable(true);

        toolBar = buildToolbar();
        add(toolBar, BorderLayout.WEST);

        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int length = 3 * Math.min(screen.width, screen.height) / 4;
        matrix.setPreferredSize(new Dimension(length, length));
        cluster.setPreferredSize(new Dimension(length / 3, length));

        pack();

        proj.addProjectListener(new ProjectListener() {
            @Override
            public void clusterChanged(final Project proj)
            {
                enableClusterButtons(proj);
            }

            @Override
            public void structureDependencyChanged(final Project proj)
            {
                enableMatrixButtons(proj);
            }

            @Override
            public void historyDependencyChanged(final Project proj)
            {
                enableMatrixButtons(proj);
            }

            @Override
            public void f2fDependencyChanged(final Project proj)
            {
                enableMatrixButtons(proj);
            }

            @Override
            public void modified(final Project proj)
            {
                // updateTitle(proj);
            }
        });

        enableClusterButtons(proj);
        enableMatrixButtons(proj);
    }

    private void buildActions()
    {
        actions.put("open-structure-dsm", new OpenStructureMatrixAction(proj, this));
        actions.put("open-history-dsm", new OpenHistoryMatrixAction(proj, this));
        actions.put("open-f2f-dsm", new Openf2fMatrixAction(proj, this));

        actions.put("redraw", new RedrawAction(proj, cluster, matrix));

        actions.put("new-clsx", new NewAction(proj));
        actions.put("load-clsx", new LoadAction(proj, this));
        actions.put("save-clsx", new SaveAction(proj, this));
        actions.put("save-clsx-as", new SaveAsAction(proj, this));

        actions.put("export-image", new ExportMatrixView2Image(proj, this, matrix));
        actions.put("export-excel", new ExportExcelAction(proj, this));
        actions.put("export-weighted-structure-dsm", new ExportWeightedStructureMatrixAction(proj,
                this, matrix));
        actions.put("export-unweighted-structure-dsm", new ExportUnweightedStructureMatrixAction(
                proj, this, matrix));
        actions.put("export-history-dsm", new ExportHistoryMatrixAction(proj, this, matrix));

        actions.put("propagation-cost", new PropagationCostAction(proj));
        actions.put("module-independent-level", new ModuleIndependentLevelAction(proj));
        actions.put("arch-issues", new ArchIssuesAction(proj));

        actions.put("quick-draw", new QuickDrawAction(proj, cluster, matrix));
        actions.put("dep-mark", new DependencyStrengthAction(proj, cluster, matrix));

        actions.put("show-history", new ShowHistoryAction(proj, cluster, matrix));
        actions.put("show-f2f", new Showf2fAction(proj, cluster, matrix));

        for (String dpTp : proj.getDpTypes()) {
            dpactions.put("show-" + dpTp, new ShowDpTypesAction(proj, cluster, matrix, dpTp));
        }

        actions.put("change-history-threshold", new ChangeHistoryDsmThresholdAction(proj, cluster,
                matrix));
        actions.put("change-f2f-threshold", new Changef2fDsmThresholdAction(proj, cluster, matrix));
        actions.put("change-top", new setTheTop(proj));
        actions.put("change-scope", new setTheScope(proj));
        actions.put("change-cochange", new setTheCochange(proj));

        actions.put("full-label", new RowLabelAction(proj, cluster, matrix));

        actions.put("about", new AboutAction(this));
        actions.put("add-ref", new AddRef(this, proj.getRefInformation()));

        actions.put("find", new FindAction(proj, this));

        actions.put("pkg-cluster", new PkgClusterAction(proj, this));
        actions.put("arch-drh-cluster", new ArchDrhClusterAction(proj, this));

        /* Repository Management Action */
        actions.put("add-project", new AddProjectAction(proj, this));
        actions.put("add-exist-project", new AddExsitProjectAction(proj, this));
        actions.put("search-project", new SearchProjectAction(proj, this));
        /* Repository Management Action */

        actions.get("quick-draw").putValue(Action.SELECTED_KEY, Boolean.TRUE);
        actions.get("dep-mark").putValue(Action.SELECTED_KEY, Boolean.FALSE);

        actions.get("change-history-threshold").putValue(Action.SELECTED_KEY, Boolean.FALSE);
        actions.get("change-f2f-threshold").putValue(Action.SELECTED_KEY, Boolean.FALSE);
        actions.get("show-history").putValue(Action.SELECTED_KEY, Boolean.FALSE);
        actions.get("show-f2f").putValue(Action.SELECTED_KEY, Boolean.FALSE);
        actions.get("full-label").putValue(Action.SELECTED_KEY, Boolean.TRUE);

        for (String dpTp : proj.getDpTypes()) {
            dpactions.get("show-" + dpTp).putValue(Action.SELECTED_KEY, proj.isTypeSelected(dpTp));
        }

    }

    private JMenuBar buildMenus()
    {
        final JMenuBar bar = new JMenuBar();

        bar.add(buildFileMenu());
        bar.add(buildMetricsMenu());
        bar.add(buildViewMenu());
        bar.add(buildClusterMenu());
        bar.add(buildHelpMenu());
        bar.add(buildRepoMenu());

        return bar;
    }

    private JMenu buildRepoMenu()
    {
        final JMenu menu = new JMenu("Repository");
        menu.setMnemonic('R');
        menu.add(new JMenuItem(actions.get("add-exist-project")));
        menu.addSeparator();
        menu.add(new JMenuItem(actions.get("add-project")));
        menu.addSeparator();
        menu.add(new JMenuItem(actions.get("search-project")));

        return menu;
    }

    private JMenu buildClusterMenu()
    {
        final JMenu menu = new JMenu("Clusters");
        menu.setMnemonic('C');

        menu.add(new JMenuItem(actions.get("pkg-cluster")));
        menu.addSeparator();
        menu.add(new JMenuItem(actions.get("arch-drh-cluster")));

        return menu;
    }

    private JMenu buildFileMenu()
    {
        final JMenu menu = new JMenu("File");
        menu.setMnemonic('F');

        menu.add(new JMenuItem(actions.get("open-structure-dsm")));
        menu.add(new JMenuItem(actions.get("open-history-dsm")));
        menu.add(new JMenuItem(actions.get("open-f2f-dsm")));

        menu.addSeparator();

        menu.add(new JMenuItem(actions.get("new-clsx")));
        menu.add(new JMenuItem(actions.get("load-clsx")));

        menu.addSeparator();

        menu.add(new JMenuItem(actions.get("save-clsx")));
        menu.add(new JMenuItem(actions.get("save-clsx-as")));

        menu.addSeparator();

        menu.add(buildExportMenu());

        menu.addSeparator();

        final JMenuItem item = new JMenuItem("Exit");
        item.setMnemonic('X');
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event)
            {
                queryClose();
            }
        });
        menu.add(item);

        return menu;
    }

    private JMenu buildExportMenu()
    {
        final JMenu menu = new JMenu("Export As");
        menu.setMnemonic('E');

        menu.add(new JMenuItem(actions.get("export-weighted-structure-dsm")));
        menu.add(new JMenuItem(actions.get("export-unweighted-structure-dsm")));
        menu.add(new JMenuItem(actions.get("export-history-dsm")));
        menu.add(new JMenuItem(actions.get("export-excel")));
        menu.add(new JMenuItem(actions.get("export-image")));

        return menu;
    }

    private JMenu buildMetricsMenu()
    {
        final JMenu menu = new JMenu("Analyze");
        menu.setMnemonic('M');

        menu.add(new JMenuItem(actions.get("propagation-cost")));
        menu.add(new JMenuItem(actions.get("module-independent-level")));
        menu.add(new JMenuItem(actions.get("arch-issues")));

        return menu;
    }

    private JMenu buildViewMenu()
    {
        final JMenu menu = new JMenu("View");
        menu.setMnemonic('V');

        menu.add(new JMenuItem(actions.get("redraw")));

        menu.addSeparator();

        menu.add(new JMenuItem(actions.get("find")));

        menu.addSeparator();

        menu.add(new JCheckBoxMenuItem(actions.get("quick-draw")));
        menu.add(new JCheckBoxMenuItem(actions.get("row-label")));
        menu.add(new JCheckBoxMenuItem(actions.get("change-history-threshold")));
        menu.add(new JCheckBoxMenuItem(actions.get("change-f2f-threshold")));
        menu.add(new JCheckBoxMenuItem(actions.get("show-history")));

        /*
         * for(String dpTp:proj.getDpTypes()){
         * menu.add(new JCheckBoxMenuItem(dpactions.get("show-"+dpTp)));
         * }
         */

        menu.add(new JCheckBoxMenuItem(actions.get("dep-mark")));

        return menu;
    }

    private JMenu buildHelpMenu()
    {
        final JMenu menu = new JMenu("Help");
        menu.setMnemonic('H');

        menu.add(new JMenuItem(actions.get("about")));
        menu.add(new JMenuItem(actions.get("add-ref")));

        return menu;
    }

    private JComponent buildToolbar()
    {

        int numItems = 17;

        JButton button;

        JCheckBox checkbox;

        final JToolBar bar = new JToolBar(JToolBar.VERTICAL);
        bar.setLayout(new GridLayout(numItems + proj.numTypes(), 1));
        bar.setOpaque(false);

        button = new JButton(actions.get("open-structure-dsm"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("open-history-dsm"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("open-f2f-dsm"));
        button.setText("");
        bar.add(button);

        button = new JButton(actions.get("redraw"));
        button.setText("");
        bar.add(button);

        bar.addSeparator();

        /*
         * button = new JButton(actions.get("new-clsx"));
         * button.setText("");
         * bar.add(button);
         * 
         * button = new JButton(actions.get("load-clsx"));
         * button.setText("");
         * bar.add(button);
         * 
         * button = new JButton(actions.get("save-clsx"));
         * button.setText("");
         * bar.add(button);
         * 
         * button = new JButton(actions.get("save-clsx-as"));
         * button.setText("");
         * bar.add(button);
         * 
         * bar.addSeparator();
         */

        checkbox = new JCheckBox(actions.get("quick-draw"));
        checkbox.setText("quick-draw");
        bar.add(checkbox);

        checkbox = new JCheckBox(actions.get("full-label"));
        checkbox.setText("full-label");
        bar.add(checkbox);

        checkbox = new JCheckBox(actions.get("dep-mark"));
        checkbox.setText("strength");
        bar.add(checkbox);

        checkbox = new JCheckBox(actions.get("show-history"));
        checkbox.setText("history");
        bar.add(checkbox);

        checkbox = new JCheckBox(actions.get("show-f2f"));
        checkbox.setText("f2f");
        bar.add(checkbox);

        bar.addSeparator();

        for (String dpTp : proj.getDpTypes()) {
            checkbox = new JCheckBox(dpactions.get("show-" + dpTp));
            checkbox.setText(dpTp);
            bar.add(checkbox);
        }

        bar.addSeparator();

        /*
         * checkbox = new JCheckBox(actions.get("change-threshold"));
         * checkbox.setText("threshold");
         * bar.add(checkbox);
         */

        button = new JButton(actions.get("change-history-threshold"));
        button.setText("history-threshold");
        bar.add(button);

        button = new JButton(actions.get("change-f2f-threshold"));
        button.setText("f2f-threshold");
        bar.add(button);

        button = new JButton(actions.get("change-cochange"));
        button.setText("cochange(4)");
        bar.add(button);

        button = new JButton(actions.get("change-scope"));
        button.setText("scope(10)");
        bar.add(button);

        button = new JButton(actions.get("change-top"));
        button.setText("top(10)");
        bar.add(button);

        return bar;

    }

    protected ClusterViewer createClusterViewer()
    {
        return new ClusterViewer(proj);
    }

    protected ClusterViewer createClusterViewer(ClusterModel model)
    {
        return new ClusterViewer(proj, model);
    }

    protected MatrixViewer createMatrixViewer()
    {
        return new MatrixViewer(proj);
    }

    protected void enableClusterButtons(final Project proj)
    {
        final boolean b = proj.getCluster() != null;
        actions.get("new-clsx").setEnabled(b);
        actions.get("load-clsx").setEnabled(b);
        actions.get("save-clsx").setEnabled(b);
        actions.get("save-clsx-as").setEnabled(b);

        actions.get("pkg-cluster").setEnabled(b);
        actions.get("arch-drh-cluster").setEnabled(b);

    }

    protected void enableMatrixButtons(final Project proj)
    {
        final boolean b = proj.getStructureDependency() != null;
        actions.get("export-weighted-structure-dsm").setEnabled(b);
        actions.get("export-unweighted-structure-dsm").setEnabled(b);
        actions.get("export-excel").setEnabled(b);
        actions.get("export-image").setEnabled(b);
        actions.get("find").setEnabled(b);
        actions.get("propagation-cost").setEnabled(b);
        actions.get("module-independent-level").setEnabled(b);
        actions.get("arch-issues").setEnabled(b);

        final boolean h = proj.getHistoryDependency() != null;
        actions.get("export-history-dsm").setEnabled(h);
    }

    protected void updateTitle(final Project proj)
    {
        final StringBuilder buf = new StringBuilder("Titan - ");
        if (proj.getClusterPath() != null)
            buf.append(proj.getClusterPath().getName());
        else
            buf.append("untitled");

        if (proj.isModified())
            buf.append("*");
        setTitle(buf.toString());
    }

    public void updateTitle(final String title)
    {
        setTitle("Titan-" + title);
    }

    protected void queryClose()
    {
        if (ActionUtilities.querySave(proj)) {
            setVisible(false);
            dispose();
        }
    }

    public void openHistory(final File file)
    {
        ((OpenHistoryMatrixAction) actions.get("open-history-dsm")).load(file);
    }

    public void openStructure(final File file)
    {
        ((OpenStructureMatrixAction) actions.get("open-structure-dsm")).load(file);
    }

    public void loadCluster(final File file)
    {
        ((LoadAction) actions.get("load-clsx")).load(file);
    }

    private static void setLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception ex) {
            setAuxilaryLookAndFeel();
        }
        finally {
            JFrame.setDefaultLookAndFeelDecorated(true);
        }
    }

    private static void setAuxilaryLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception inner) {
            // ignore
        }
    }

    public static void main(final String[] args)
    {
        setLookAndFeel();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                final Viewer app = new Viewer();
                app.setLocationRelativeTo(null);
                if (args.length > 0)
                    app.openStructure(new File(args[0]));
                if (args.length > 1)
                    app.openHistory(new File(args[2]));
                if (args.length > 2)
                    app.loadCluster(new File(args[2]));

                app.setVisible(true);

            }
        });
    }

    public void update()
    {

        dpactions = new HashMap<String, Action>();
        for (String dpTp : proj.getDpTypes()) {
            dpactions.put("show-" + dpTp, new ShowDpTypesAction(proj, cluster, matrix, dpTp));
        }

        for (String dpTp : proj.getDpTypes()) {
            dpactions.get("show-" + dpTp).putValue(Action.SELECTED_KEY, proj.isTypeSelected(dpTp));
        }

        this.remove(toolBar);
        toolBar = buildToolbar();
        add(toolBar, BorderLayout.WEST);
        this.repaint();

    }
}
