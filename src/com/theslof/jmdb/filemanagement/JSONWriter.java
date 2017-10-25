package com.theslof.jmdb.filemanagement;

import com.google.gson.Gson;
import com.theslof.jmdb.Database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter implements IWriter<Database> {
    @Override
    public void writeToFile(Database db, File file) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(file);
        writer.write(gson.toJson(db));
        writer.close();
    }
}
