package edu.drexel.cs.rise.titan.action.analyze;

//import hotpotdetector.runnable.a;

import hotpotdetector.runnable.a;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.drexel.cs.rise.ArchDRH.ArchDrh;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.util.ClusterBuilder;
import edu.drexel.cs.rise.util.WeightedDigraph;

//import hotpotdetector.runnable.archIssueDetectorRunnable;

public class ArchIssuesAction extends AbstractAction
{

    private static final long serialVersionUID = 10L;
    protected final Project proj;

    protected JFrame ArchIssue_pannel;
    protected JTextArea progress;
    protected Thread td;

    public ArchIssuesAction(final Project proj)
    {
        this.proj = proj;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Detect Architecture Issues");
        // putValue(MNEMONIC_KEY, (int)'F');
        putValue(SHORT_DESCRIPTION, "Detect Architecture Issues");

        ArchIssue_pannel = new JFrame();
        ArchIssue_pannel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                td.stop();
            }
        });

        ArchIssue_pannel.setLayout(new GridLayout(1, 1));
        ArchIssue_pannel.setName("Architecture Issues");
        ArchIssue_pannel.setTitle("Architecture Issues");

        addDialog(ArchIssue_pannel);
    }

    private void addDialog(JFrame p)
    {

        progress = new JTextArea(10, 10);
        JScrollPane scrollbar = new JScrollPane(progress, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        progress.setEditable(false);
        p.add(scrollbar);

    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {

        progress.setText("");
        ArchIssue_pannel.setSize(400, 400);
        ArchIssue_pannel.setVisible(true);

        WeightedDigraph<String> sdsm = proj.getStructureDependency();
        WeightedDigraph<String> hdsm = proj.getHistoryDependency();

        // ClusterSet archRoot = (ClusterSet) edu.drexel.cs.rise.ArchDRH.a.a(sdsm.toDigraph());

        ClusterSet archRoot = (ClusterSet) ArchDrh.buildArchDRHRecursive(sdsm.toDigraph());

        ClusterBuilder cb = new ClusterBuilder();
        cb.buildClusters(proj.getStructureDependency().vertices());
        ClusterSet pkgRoot = cb.getCluster();

        progress.append("The input dsm size is " + sdsm.vertices().size() + "\n");

        // PrintStream writer = new PrintStream(new TextAreaOutputStream(progress));
        // String outpath =
        // proj.getCurrentDirectory().getAbsolutePath()+File.pathSeparatorChar+proj.getProjectName();
        File outdir = new File(proj.getCurrentDirectory().getAbsolutePath(), "archIssues-"
                + proj.getProjectName());
        outdir.mkdir();
        boolean flag = true;
        if (!outdir.exists()) {
            flag = false;
            progress.append("Can't create output directory\n" + outdir.getAbsolutePath());
        }
        ;

        if (flag) {
            /*
             * a myIssues = new a(progress, outdir, proj.getDpTypes(), sdsm,
             * hdsm, archRoot, pkgRoot);
             * myIssues.a(proj.getCochange(), proj.getTop(), proj.getScope());
             */

            a myIssues = new a(progress, outdir, proj.getDpTypes(), sdsm, hdsm, archRoot, pkgRoot);

            myIssues.a(proj.getCochange(), proj.getTop(), proj.getScope());

            td = new Thread(myIssues);
            td.start();

        }

    }
}
