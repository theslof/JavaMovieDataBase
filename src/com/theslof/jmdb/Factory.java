package com.theslof.jmdb;

import com.theslof.jmdb.filemanagement.*;
import com.theslof.jmdb.filters.*;

import java.util.ArrayList;

public class Factory {
    public static ArrayList<IFilter<Movie>> createFilter(){
        return new ArrayList<>();
    }

    public static IFilter<Movie> filter(String s){
        StringFilter filter = new StringFilter();
        filter.setString(s);
        return filter;
    }

    public static IFilter<Movie> filter(ArrayList<Genre> g){
        GenreFilter filter = new GenreFilter();
        filter.addGenres(g);
        return filter;
    }

    public static IFilter<Movie> filter(Person p){
        PersonFilter filter = new PersonFilter();
        filter.setPerson(p);
        return filter;
    }
}
