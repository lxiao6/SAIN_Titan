/*
 * MatrixViewer.java
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
import java.awt.FontMetrics;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import edu.drexel.cs.rise.DesignSpace.Utilities.ComplexityChecker;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ProjectListener;
import edu.drexel.cs.rise.titan.model.MatrixModel;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.util.Matrix;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class MatrixViewer extends JPanel
{
    Runtime runtime = Runtime.getRuntime();
    private static final long serialVersionUID = 10L;

    private final MatrixModel model;
    private final JScrollPane body;
    private final JTable table;

    private final DefaultListModel rows;
    private final JList headers;

    private final TableCell renderer;
    protected final Project proj;

    public MatrixViewer(final Project proj)
    {
        this(proj, new MatrixModel(proj));
    }

    public MatrixViewer(final Project proj, final MatrixModel model)
    {
        this.proj = proj;
        this.model = model;
        this.table = new JTable(model);

        this.rows = new DefaultListModel();
        this.headers = new JList(rows);

        this.body = new JScrollPane(table);
        this.renderer = new TableCell();

        initialize();
    }

    private void initialize()
    {

        table.setDefaultRenderer(String.class, renderer);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDoubleBuffered(true);

        final JTableHeader columns = table.getTableHeader();
        // columns.setDefaultRenderer(new RowHeader(RowHeader.CENTER));
        columns.setReorderingAllowed(false);
        columns.setResizingAllowed(true);
        columns.setAutoscrolls(true);

        headers.setCellRenderer(new RowHeader(RowHeader.LEADING));
        headers.setOpaque(false);

        body.setDoubleBuffered(true);

        setLayout(new BorderLayout());
        add(body);

        // final Project proj = Project.getInstance();
        proj.addProjectListener(new Listener());
    }

    public MatrixModel getModel()
    {
        return model;
    }

    public DefaultListModel getRows()
    {
        return rows;
    }

    public JScrollBar getVerticalScrollBar()
    {
        return body.getVerticalScrollBar();
    }

    protected class Listener implements ProjectListener
    {
        @Override
        public void clusterChanged(final Project proj)
        {
            update(proj);
        }

        @Override
        public void structureDependencyChanged(final Project proj)
        {
            // wait for cluster to change
        }

        @Override
        public void historyDependencyChanged(final Project proj)
        {
            // wait for cluster to change
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

    public void update(final Project proj)
    {
        System.out
                .println("----------------------------------------------------------------------------------------");
        ComplexityChecker.getMemoryUsage(runtime, "Start update Memory");
        long t1 = ComplexityChecker.getTime("Start update: ");
        ClusterUtilities.buildMatrix(proj, model);
        final Matrix<Integer, String> matrix = model.getData();

        rows.clear();
        if (proj.isRowLabeled()) {
            for (String s : matrix.getLabels()) {
                rows.addElement(s + "  ");
            }
        }
        else {
            int i = 1;
            for (String name : matrix.getLabels()) {
                int index = 0;
                if (name.contains(".")) {
                    index = name.lastIndexOf(".");
                }
                name = name.substring(index + 1);
                rows.addElement(String.valueOf(i) + " " + name);
                i++;
            }
            /*
             * for (int i = 1; i <= matrix.length(); ++i){
             * String name = String.valueOf(i);
             * int index = 0;
             * if(name.contains(".")){
             * index = name.lastIndexOf(".");
             * }
             * name = name.substring(index);
             * System.out.println(name);
             * rows.addElement(name);
             * 
             * }
             */
        }

        headers.setFixedCellHeight(table.getRowHeight());
        body.setRowHeaderView(headers);

        renderer.recache(matrix.length(), matrix.length(), model.getModules());
        renderer.violation(matrix.length(), matrix.length(), model);

        TableColumnModel columnModel = table.getColumnModel();
        for (int col = 0; col < matrix.length(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            table.getTableHeader().setResizingColumn(tableColumn); /*
                                                                    * This is missing at the very
                                                                    * beginning
                                                                    */
            tableColumn.setWidth(getMaxWidth(col));
        }
        long t2 = ComplexityChecker.getTime("End update: ");
        System.out.println("Update time(s) " + (t2 - t1) / 1000);
        ComplexityChecker.getMemoryUsage(runtime, "End update Memory");
        System.out
                .println("----------------------------------------------------------------------------------------");
    }

    private int getMaxWidth(int col)
    {

        final FontMetrics metrics = table.getFontMetrics(table.getFont());
        int max = 0;// metrics.stringWidth("("+model.getRowCount()+")");
        for (int row = 0; row < model.getRowCount(); row++) {
            String s = model.getValueAt(row, col);
            int l = metrics.stringWidth(s + "   "); /* This is a hack */
            if (l > max) {
                max = l;
            }
        }
        return max;
    }

    public JTable getTable()
    {
        return table;
    }
}
