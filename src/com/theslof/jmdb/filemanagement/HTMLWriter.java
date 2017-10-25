package com.theslof.jmdb.filemanagement;

import com.theslof.jmdb.Database;
import com.theslof.jmdb.Movie;
import com.theslof.jmdb.Person;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLWriter implements IWriter<Database> {
    @Override
    public void writeToFile(Database db, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fileWriter);

        out.write("<!DOCTYPE html>\n<html>\n");

        for (Movie m : db.getMovies()) {
            String name = m.getName();
            String year = "" + m.getReleaseYear();
            String director = m.getDirector().toString();

            if (!name.isEmpty())
                out.write("<div><h1>" + m.getName() + "</h1>");
            if (!year.isEmpty())
                out.write("<h2>Year of release: " + m.getReleaseYear() + "</h2>");
            if (!director.isEmpty())
                out.write("<h3>Director: " + m.getDirector().getName() + "</h3>");
            if (m.getActors().size() > 0) {
                out.write("<h3>Actors:</h3>");
                for (Person p : m.getActors()) {
                    out.write(p.getName() + "<br />");
                }
            }
            out.write("</div><br /><hr />\n");
        }

        out.write("</html>");

        out.close();
        fileWriter.close();
    }
}
