/*
 * ExportAction.java
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
package edu.drexel.cs.rise.titan.action.export;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.action.FileAction;
import edu.drexel.cs.rise.titan.ui.MatrixViewer;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public class ExportMatrixView2Image extends FileAction
{
    private static final long serialVersionUID = 10L;
    protected final Project proj;
    protected final MatrixViewer matrix;
    protected final String extension = ".png";

    public ExportMatrixView2Image(final Project proj, final Component parent, MatrixViewer matrix)
    {
        super(proj, parent);
        this.proj = proj;
        this.matrix = matrix;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Save to iMage...");
        putValue(MNEMONIC_KEY, (int) 'x');
        putValue(SHORT_DESCRIPTION, "Export iMage");
        putValue(SMALL_ICON, IconFactory.load("save-image.png"));

        final FileFilter filterPNG = new FileNameExtensionFilter("Save to Png (*.png)", "." + "png");

        chooser.addChoosableFileFilter(filterPNG);
        chooser.setDialogTitle("Export MatrixView to iMage");
        chooser.setMultiSelectionEnabled(false);
    }

    @Override
    public void actionPerformed(final ActionEvent event)
    {
        prepare();
        if (showSaveDialog() != JFileChooser.CANCEL_OPTION) {
            final File path = getPathWithExtension(chooser.getSelectedFile(), extension);
            if (!confirmOverwrite(path))
                return;

            export(path);
        }
    }

    private void export(final File path)
    {
        BufferedImage iMage = createImage();
        try {
            ImageIO.write(iMage, "png", path);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public BufferedImage createImage()
    {

        JTable table = matrix.getTable();

        int w = table.getWidth();
        int h = table.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        table.printAll(g);
        return bi;
    }

}
