package com.theslof.jmdb.filemanagement;

import com.theslof.jmdb.Database;

import java.io.*;

public class SerializeWriter implements IWriter<Database> {
    @Override
    public void writeToFile(Database db, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(db);
        out.close();
        stream.close();
    }
}
