package edu.drexel.cs.rise.titan.action.analyze;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.util.WeightedDigraph;

public class PropagationCostAction extends AbstractAction
{

    private static final long serialVersionUID = 10L;
    protected final Project proj;

    protected JFrame PC_pannel;
    protected JTextArea progress;
    protected Thread td;

    public PropagationCostAction(final Project proj)
    {
        this.proj = proj;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Propagation Cost");
        // putValue(MNEMONIC_KEY, (int)'F');
        putValue(SHORT_DESCRIPTION, "Compute the propagation cost");

        PC_pannel = new JFrame();
        PC_pannel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                td.stop();
            }
        });

        PC_pannel.setLayout(new GridLayout(1, 1));
        PC_pannel.setName("Propagation Cost");
        PC_pannel.setTitle("Propagation Cost");

        addDialog(PC_pannel);
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
        PC_pannel.setSize(400, 400);
        PC_pannel.setVisible(true);

        WeightedDigraph<String> dsm = proj.getStructureDependency();
        WeightedDigraph<String> subdsm = proj.getDPControl().filter(dsm);

        progress.append("The input dsm size is " + dsm.vertices().size() + "\n");

        edu.drexel.cs.rise.pc.a mypc = new edu.drexel.cs.rise.pc.a(progress,
                proj.getRefInformation(), proj.getCurProect(), dsm, subdsm);
        // a mypc = new a(progress,proj.getRefInformation(),proj.getCurProect(), dsm, subdsm);
        td = new Thread(mypc);
        td.start();

    }

}
