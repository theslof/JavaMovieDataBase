package com.theslof.jmdb.filters;

import com.theslof.jmdb.Movie;

public abstract class MovieSort {
    public static int none(Movie o1, Movie o2){
        return 1;
    }

    public static int name(Movie m1, Movie m2){
        return m1.getName().compareTo(m2.getName());
    }

    public static int year(Movie m1, Movie m2) {
        return m2.getReleaseYear() - m1.getReleaseYear();
    }

    public static int score(Movie m1, Movie m2) {
        return (int) ((m2.getScore() - m1.getScore()) * 10);
    }

}
