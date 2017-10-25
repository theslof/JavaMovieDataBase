package com.theslof.jmdb.filemanagement;

import com.theslof.jmdb.Database;
import com.theslof.jmdb.Movie;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLWriter implements IWriter<Database> {
    @Override
    public void writeToFile(Database db, File file) throws IOException {
        try {
            DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = docFac.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element root = doc.createElement("database");
            doc.appendChild(root);

            int index = 0;

            for (Movie m : db.getMovies()) {
                Element movie = doc.createElement("movie_" + index++);
                root.appendChild(movie);

                Attr name = doc.createAttribute("name");
                name.setValue(m.getName());
                movie.setAttributeNode(name);

                Attr year = doc.createAttribute("year");
                year.setValue("" + m.getReleaseYear());
                movie.setAttributeNode(year);

                Attr genres = doc.createAttribute("genres");
                genres.setValue(m.genresAsString());
                movie.setAttributeNode(genres);

                if (m.getDirector() != null) {
                    Attr director = doc.createAttribute("director");
                    director.setValue(m.getDirector().getName());
                    movie.setAttributeNode(director);
                }
                for (int i = 0; i < m.getActors().size(); i++) {
                    Element crew = doc.createElement("crew_" + i);
                    movie.appendChild(crew);
                    crew.setAttribute("Actor", m.getActors().get(i).getName());
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
