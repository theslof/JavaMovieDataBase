package com.theslof.jmdb.filters;

import com.theslof.jmdb.Movie;
import com.theslof.jmdb.Person;

public class PersonFilter implements IFilter<Movie> {
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public boolean filter(Movie o) {
        return o.hasCrew(person);
    }
}
