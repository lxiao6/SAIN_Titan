/*
 * MatrixModel.java
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
package edu.drexel.cs.rise.titan.model;

import java.text.DecimalFormat;

import javax.swing.table.DefaultTableModel;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.util.Interval;
import edu.drexel.cs.rise.util.Matrix;
import edu.drexel.cs.rise.util.TreeNode;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class MatrixModel extends DefaultTableModel
{
    private static final long serialVersionUID = 10L;

    private Matrix<Integer, String> structure_data = null;
    private Matrix<Integer, String> history_data = null;
    private Matrix<Double, String> f2f_data = null;
    private Matrix<Integer, String> clusters_data = null;
    private TreeNode<Interval> modules;
    protected final Project proj;

    private enum data_type
    {
        n_to_n, one_to_one, history, f2f
    }

    public MatrixModel(final Project proj)
    {
        this(proj, null);
    }

    public MatrixModel(final Project proj, final Matrix<Integer, String> data)
    {
        this.proj = proj;
        this.structure_data = data;
    }

    public final Matrix<Integer, String> getData()
    {
        return structure_data;
    }

    public final void setHistoryData(final Matrix<Integer, String> data)
    {
        this.history_data = data;
        super.fireTableStructureChanged();
    }

    public final Matrix<Integer, String> getHistoryData()
    {
        return history_data;
    }

    public final void setf2fData(final Matrix<Double, String> data)
    {
        this.f2f_data = data;
        super.fireTableStructureChanged();
    }

    public final Matrix<Double, String> getf2fData()
    {
        return f2f_data;
    }

    public final void setData(final Matrix<Integer, String> data)
    {
        setStructureData(data, null);
    }

    public final void setStructureData(final Matrix<Integer, String> data,
            final TreeNode<Interval> modules)
    {
        this.structure_data = data;
        this.modules = modules;
        super.fireTableStructureChanged();
    }

    public final TreeNode<Interval> getModules()
    {
        return modules;
    }

    public final void setModules(final TreeNode<Interval> modules)
    {
        this.modules = modules;
        super.fireTableDataChanged();
    }

    @Override
    public int getColumnCount()
    {
        return structure_data != null ? structure_data.length() : 0;
    }

    @Override
    public String getColumnName(int column)
    {
        return String.valueOf(column + 1);
    }

    @Override
    public int getRowCount()
    {
        return getColumnCount();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    @Override
    public String getValueAt(int row, int column)
    {
        String rtVal = "";
        if (row == column)
            rtVal = "(" + (row + 1) + ")";
        else {

            String structureDp = translateInt2String(structure_data.get(row, column),
                    getDataType(row, column));

            if (!structureDp.isEmpty()) {
                rtVal = structureDp;
            }

            String historyCoupling = "";
            String f2fValue = "";

            if (history_data != null && proj.isHistory()) {

                historyCoupling = translateInt2String(history_data.get(row, column),
                        data_type.history);

                if (!historyCoupling.isEmpty()) {

                    rtVal = rtVal + "," + historyCoupling;

                }
            }

            if (f2f_data != null && proj.isf2f()) {

                f2fValue = translateDouble2String(f2f_data.get(row, column), data_type.f2f);

                if (!f2fValue.isEmpty()) {

                    rtVal = rtVal + "," + f2fValue;

                }

            }

        }

        return rtVal;
    }

    private data_type getDataType(int row, int column)
    {

        int v = clusters_data.get(row, column);
        if (v == 0) {
            return data_type.n_to_n;
        }
        else {
            return data_type.one_to_one;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    protected final String translateDouble2String(Double weight, data_type type)
    {
        if (weight == null || weight == 0)
            return "";
        if (type.equals(data_type.f2f)) {
            if (weight > proj.getf2fDsmThreshold()) {
                if (proj.isWeighted()) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    return df.format(weight);
                }

                else
                    return "x";
            }
        }
        return "";

    }

    protected final String translateInt2String(Integer weight, data_type type)
    {
        if (weight == null || weight.intValue() == 0)
            return "";
        if (type.equals(data_type.n_to_n)) {

            if (proj.isWeighted())
                return weight.toString();
            else
                return "x";
        }
        if (type.equals(data_type.one_to_one)) {

            if (proj.isWeighted())
                return proj.getDPControl().text(weight);
            else
                return "x";
        }
        if (type.equals(data_type.history)) {
            if (weight > proj.getHistoryDsmThreshold()) {
                if (proj.isWeighted())
                    return weight.toString();
                else
                    return "x";
            }
        }

        return "";
    }

    public int getSecondaryValue(int row, int col)
    {

        if (history_data != null && structure_data != null && row != col && proj.isHistory()) {

            return history_data.get(row, col);

        }
        return 0;

    }

    public boolean violation(int row, int col)
    {
        // final Project proj = Project.getInstance();
        if (history_data != null && structure_data != null && row != col && proj.isHistory()) {
            if (structure_data.get(row, col) == 0
                    && (history_data.get(row, col) > proj.getHistoryDsmThreshold()))
                return true;
        }
        return false;
    }

    public void setClusterData(Matrix<Integer, String> cluster_matrix)
    {

        clusters_data = cluster_matrix;

    }
}
