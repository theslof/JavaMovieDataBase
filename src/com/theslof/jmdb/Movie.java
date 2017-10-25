package com.theslof.jmdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Movie implements Serializable {
    private String name;
    private int releaseYear = 1878;
    private float score;
    private Person director = Person.none();
    private ArrayList<Genre> genres = new ArrayList<>();
    private ArrayList<Person> actors = new ArrayList<>();

    public Movie(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException();
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        if(genres == null)
            throw new IllegalArgumentException();
        this.genres = genres;
    }

    public ArrayList<Person> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Person> actors) {
        if(actors == null)
            throw new IllegalArgumentException();
        this.actors = actors;
    }

    public void addActor(Person actor) {
        if (actor != null && !this.actors.contains(actor))
            this.actors.add(actor);
    }

    public void removeActor(Person actor) {
        actors.remove(actor);
    }

    public void addGenre(Genre g) {
        if(g != null && g != Genre.NONE && !genres.contains(g))
            genres.add(g);
    }

    public String genresAsString() {
        StringJoiner s = new StringJoiner(", ");
        for (Genre g : genres) {
            s.add(Genre.toString(g));
        }

        return s.toString();
    }

    public boolean hasCrew(Person person) {
        if (person == null)
            return true;
        return actors.contains(person) || getDirector() == person;
    }

    public boolean hasCrew(String s) {
        if(s == null)
            throw new IllegalArgumentException();
        if (s.isEmpty())
            return true;
        for (Person p : actors) {
            if (p.getName().toUpperCase().contains(s.toUpperCase()))
                return true;
        }
        return getDirector().getName().toUpperCase().contains(s.toUpperCase());
    }

    public boolean isGenre(ArrayList<Genre> genres) {
        if(genres == null)
            throw new IllegalArgumentException();
        if (genres.isEmpty())
            return true;
        for (Genre g : genres) {
            if (isGenre(g))
                return true;
        }
        return false;
    }

    public boolean isGenre(Genre g) {
        return this.genres.contains(g);
    }

    @Override
    public String toString() {
        return getName();
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }
}
