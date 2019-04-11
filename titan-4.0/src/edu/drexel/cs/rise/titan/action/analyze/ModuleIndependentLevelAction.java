package edu.drexel.cs.rise.titan.action.analyze;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.drexel.cs.rise.mimArchDRH.b;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.util.WeightedDigraph;

//import edu.drexel.cs.rise.mimArchDRH.mimArchDrhRunnable;

public class ModuleIndependentLevelAction extends AbstractAction
{

    private static final long serialVersionUID = 10L;
    protected final Project proj;

    protected JFrame MIM_pannel;
    protected JTextArea progress;
    protected Thread td;

    public ModuleIndependentLevelAction(final Project proj)
    {
        this.proj = proj;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Module Independence Level");
        // putValue(MNEMONIC_KEY, (int)'F');
        putValue(SHORT_DESCRIPTION, "Compute module independence level");

        MIM_pannel = new JFrame();
        MIM_pannel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                td.stop();
            }
        });

        MIM_pannel.setLayout(new GridLayout(1, 1));
        MIM_pannel.setName("Module Independence Level");
        MIM_pannel.setTitle("Module Independence Level");

        addDialog(MIM_pannel);
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
        MIM_pannel.setSize(400, 400);
        MIM_pannel.setVisible(true);

        WeightedDigraph<String> dsm = proj.getStructureDependency();
        WeightedDigraph<String> subdsm = proj.getDPControl().filter(dsm);

        progress.append("The input dsm size is " + dsm.vertices().size() + "\n");

        b mypc = new b(progress, proj.getRefInformation(), proj.getCurProect(), dsm.toDigraph(),
                subdsm.toDigraph());

        td = new Thread(mypc);
        td.start();

    }

}
