/*
 * TableCell.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import edu.drexel.cs.rise.titan.model.MatrixModel;
import edu.drexel.cs.rise.util.Interval;
import edu.drexel.cs.rise.util.TreeNode;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class TableCell extends JLabel implements TableCellRenderer
{
    private static final long serialVersionUID = 10L;
    private static final Color white = new Color(255, 255, 255);
    private static final Color red = new Color(255, 0, 0);
    private static final Color pink = new Color(255, 102, 204);
    private static final Color[] colors = new Color[] { new Color(255, 255, 255),// white
            new Color(102, 204, 255),// blue
            new Color(102, 255, 204),// green
            new Color(255, 204, 102),// yellow
            new Color(204, 102, 255),// purple
            new Color(255, 102, 204),// hot pink
            new Color(255, 0, 0) };

    protected int max_level = 1;
    protected int[][] cache = null;
    protected int[][] violation = null;

    public TableCell()
    {	// super("");
        super("", CENTER);
        initialize();

    }

    private void initialize()
    {
        setOpaque(true);
        addMouseMotionListener(new MouseMotionAdapter() {
            // override the method
            public void mouseClicked(MouseEvent arg0)
            {
                // to do .........................
            }
        });
    }

    public void recache(int width, int height, final TreeNode<Interval> modules)
    {
        cache = new int[width][height];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                cache[row][col] = computeModuleLevel(0, row, col, modules);
                if (cache[row][col] > max_level) {
                    max_level = cache[row][col];
                }
            }
        }
    }

    public void violation(int width, int height, final MatrixModel model)
    {
        violation = new int[width][height];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {

                int value = model.getSecondaryValue(row, col);

                if (value == 2)
                    violation[row][col] = -1;

                if (value == 3)
                    violation[row][col] = 1;

                if (value == 1)
                    violation[row][col] = 3;
                /*
                 * if(model.violation(row,col))
                 * violation[row][col] = colors.length-1;
                 * else violation[row][col] = -1;
                 */
            }
        }
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        final String text = value.toString();
        setText(text);

        final MatrixModel model = (MatrixModel) table.getModel();
        final int index = cache[row][column];
        final int violation_index = violation[row][column];

        MatteBorder border = new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);
        setBorder(border);
        if (index >= 0) {
            float shade = ((float) max_level - (float) index) / (float) max_level;
            // System.out.println("cell (row, col) ("+row+","+column+") shade "+shade);
            int v = (int) (255 * shade);
            Color bc = new Color(v, v, v);
            Color fc = IdealTextColor(bc);
            setBackground(bc);
            setForeground(fc);
        }

        if (violation_index > 0)
            setBackground(colors[violation_index]);

        if (text.length() == 0)
            setToolTipText(null);
        else if (!(text.startsWith("(") && text.endsWith(")"))) {
            final String src = model.getData().getLabel(row);
            final String dest = model.getData().getLabel(column);
            setToolTipText(src + " -> " + dest);
        }

        return this;
    }

    public Color IdealTextColor(Color bg)
    {
        int nThreshold = 105;
        int bgDelta = (int) ((bg.getRed() * 0.299) + (bg.getGreen() * 0.587) + (bg.getBlue() * 0.114));

        Color foreColor = (255 - bgDelta < nThreshold) ? Color.black : Color.white;
        return foreColor;
    }

    protected static final int computeModuleLevel(int level, int x, int y,
            final TreeNode<Interval> node)
    {
        if (node == null || !contains(node.getValue(), x, y))
            return level;

        for (TreeNode<Interval> child : node) {
            if (contains(child.getValue(), x, y))
                return computeModuleLevel(level + 1, x, y, child);
        }

        return level;
    }

    protected static final boolean contains(final Interval inv, int x, int y)
    {
        return inv.contains(x) && inv.contains(y);
    }
}
