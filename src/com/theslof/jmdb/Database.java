package com.theslof.jmdb;

import com.google.gson.Gson;
import com.theslof.jmdb.filters.IFilter;
import com.theslof.jmdb.filters.ISort;
import com.theslof.jmdb.filters.MovieSort;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database implements IDatabase, Serializable {
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();
    private transient DefaultListModel<Movie> movieModel = new DefaultListModel<>();
    private transient ArrayList<IFilter<Movie>> filters = new ArrayList<>();
    private transient ISort<Movie> sort = MovieSort::none;

    private void add(Movie movie) {
        movies.add(movie);
        movieModel.addElement(movie);
    }

    private void add(Person person) {
        people.add(person);
    }

    @Override
    public void remove(Movie movie) {
        movies.remove(movie);
        movieModel.removeElement(movie);
    }

    @Override
    public Person newPerson(String text) {
        Person p = getPersonByName(text);
        if (p == null) {
            p = new Person(text);
            this.add(p);
        }
        return p;
    }

    @Override
    public Movie newMovie(String text) {
        Movie m = getMovieByName(text);
        if (m == null) {
            m = new Movie(text);
            this.add(m);
        }
        return m;
    }

    private Movie getMovieByName(String text) {
        for (Movie m : getMovies()) {
            if (m.getName().equalsIgnoreCase(text))
                return m;
        }
        return null;
    }

    private Person getPersonByName(String text) {
        for (Person p : getPeople()) {
            if (p.getName().equals(text))
                return p;
        }
        return null;
    }

    @Override
    public void remove(Person person) {
        people.remove(person);
        for (Movie m : getMoviesBy(person)) {
            m.removeActor(person);
            if (m.getDirector() == person)
                m.setDirector(null);
        }
    }

    public Movie get(int index) {
        return movieModel.get(index);
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public DefaultListModel<Movie> getMovieModel() {
        return movieModel;
    }

    public ArrayList<Movie> getMoviesBy(Person p) {
        return (ArrayList<Movie>) movies.stream()
                .filter(m -> m.hasCrew(p))
                .collect(Collectors.toList());
    }

    public ArrayList<Movie> getMoviesBy(Genre... g) {
        return (ArrayList<Movie>) movies.stream()
                .filter(m -> m.isGenre((ArrayList<Genre>) Arrays.asList(g)))
                .collect(Collectors.toList());
    }

    public ArrayList<Movie> getMoviesBy(String s) {
        return (ArrayList<Movie>) movies.stream()
                .filter(m -> m.getName().contains(s) || m.hasCrew(s))
                .collect(Collectors.toList());
    }

    public void sortByName() {
        sort = MovieSort::name;
        updateModel();
    }

    public void sortByYear() {
        sort = MovieSort::year;
        updateModel();
    }

    public void sortByScore() {
        sort = MovieSort::score;
        updateModel();
    }

    public void sortByNone() {
        sort = MovieSort::none;
        updateModel();
    }

    @Override
    public void updateModel() {
        movieModel.clear();
        Stream<Movie> stream = movies.stream();

        for (IFilter<Movie> filter : filters) {
            stream = stream.filter(filter::filter);
        }

        stream.sorted(sort::sort).forEach(movieModel::addElement);
    }

    @Override
    public ArrayList<IFilter<Movie>> getFilter() {
        if (filters == null)
            filters = new ArrayList<>();
        return filters;
    }

    public void setFilter(ArrayList<IFilter<Movie>> filters) {
        this.filters = filters;
    }

    @Override
    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setMovieModel(DefaultListModel<Movie> movieModel) {
        this.movieModel = movieModel;
    }

    public static Database readObject(File file) throws IOException, ClassNotFoundException {
        FileInputStream stream = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(stream);
        Database db = (Database) in.readObject();

        in.close();
        stream.close();

        db.setMovieModel(new DefaultListModel<>());
        db.setFilter(Factory.createFilter());
        db.sort = MovieSort::none;
        db.updateModel();

        return db;
    }

    public static Database readObjectJSON(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        Gson gson = new Gson();
        Database db = gson.fromJson(reader, Database.class);

        reader.close();

        // JSON doesn't respect objects, so every movie gets a unique Person object for every actor.
        // Loop through all movies and replace their actors with the ones from our main list.
        for (Movie m : db.getMovies()) {
            ArrayList<Person> actors = new ArrayList<>();
            for (Person p : m.getActors()) {
                Person p1 = db.newPerson(p.getName());
                actors.add(p1);
            }
            m.setActors(actors);
            m.setDirector(db.newPerson(m.getDirector().getName()));
        }

        db.getPeople().remove(Person.none());
        db.setMovieModel(new DefaultListModel<>());
        db.setFilter(Factory.createFilter());
        db.sort = MovieSort::none;
        db.updateModel();
        return db;
    }

    @Override
    public void importFromIMDB(String text) throws IOException {
        Document doc = Jsoup.connect("http://www.imdb.com/title/" + text).get();
        Element movieInfo = doc.getElementById("title-overview-widget");
        Elements titleWrapper = movieInfo.getElementsByClass("title_wrapper");
        Elements titleBar = titleWrapper.get(0).getElementsByAttributeValue("itemprop", "name");
        String title = titleBar.get(0).ownText();
        String year = titleBar.get(0).getElementsByTag("a").get(0).ownText();
        String director = movieInfo.getElementsByAttributeValue("itemprop", "director").get(0)
                .getElementsByAttributeValue("itemprop", "name").get(0).ownText();
        Elements elementsActors = movieInfo.getElementsByAttributeValue("itemprop", "actors");
        Elements elementsGenres = movieInfo.getElementsByAttributeValue("itemprop", "genre");
        String rating = movieInfo.getElementsByAttributeValue("itemprop", "ratingValue").get(0).ownText();

        Movie movie = newMovie(title.trim());
        movie.setReleaseYear(Integer.parseInt(year.trim()));
        movie.setScore((float) Double.parseDouble(rating.trim()));
        movie.setDirector(newPerson(director.trim()));

        for (Element e : elementsActors) {
            String actor = e.getElementsByAttributeValue("itemprop", "name").get(0).ownText();
            movie.addActor(this.newPerson(actor.trim()));
        }

        for (Element e : elementsGenres) {
            String genre = e.ownText();
            movie.addGenre(Genre.fromString(genre.trim()));
        }
    }
}
