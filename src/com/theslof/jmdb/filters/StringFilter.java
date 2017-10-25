package com.theslof.jmdb.filters;

import com.theslof.jmdb.Movie;
import com.theslof.jmdb.Person;

public class StringFilter implements IFilter<Movie> {
    private String filterString;

    public void setString(String s) {
        filterString = s.toLowerCase();
    }

    public String getString() {
        return filterString;
    }

    @Override
    public boolean filter(Movie o) {
        if (o.getName().toLowerCase().contains(filterString) ||
                ("" + o.getReleaseYear()).contains(filterString) ||
                o.getDirector().getName().toLowerCase().contains(filterString))
            return true;
        for (Person p : o.getActors()) {
            if (p.getName().toLowerCase().contains(filterString))
                return true;
        }

        return false;
    }
}
