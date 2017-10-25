package com.theslof.jmdb;

import com.theslof.jmdb.filemanagement.IWriter;
import com.theslof.jmdb.filemanagement.WriterFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CancellationException;

public abstract class SaveToFile {
    public static File selectFile(String description, String fileType) throws CancellationException, FileNotFoundException {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter(description, fileType));
        int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
        if(returnVal == JFileChooser.CANCEL_OPTION)
            throw new CancellationException();

        throw new FileNotFoundException();
    }

    public static IWriter<Database> createWriter(String fileType) {
        return WriterFactory.createWriter(fileType);
    }
}
