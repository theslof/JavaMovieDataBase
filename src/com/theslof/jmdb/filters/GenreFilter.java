package com.theslof.jmdb.filters;

import com.theslof.jmdb.Genre;
import com.theslof.jmdb.Movie;

import java.util.ArrayList;

public class GenreFilter implements IFilter<Movie> {
    private ArrayList<Genre> genres = new ArrayList<>();

    public void addGenres(ArrayList<Genre> g){
        genres.addAll(g);
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    @Override
    public boolean filter(Movie o) {
        for (Genre g:genres) {
            if(o.isGenre(g))
                return true;

        }
        return false;
    }
}
