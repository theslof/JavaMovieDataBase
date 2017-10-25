package com.theslof.jmdb;

import com.theslof.jmdb.filters.IFilter;

import java.io.IOException;
import java.util.ArrayList;

public interface IDatabase {
    ArrayList<Person> getPeople();

    ArrayList<IFilter<Movie>> getFilter();

    void updateModel();

    void remove(Person person);

    void remove(Movie movie);

    Person newPerson(String text);

    Movie newMovie(String text);

    void importFromIMDB(String text) throws IOException;
}
