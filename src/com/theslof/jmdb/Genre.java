package com.theslof.jmdb;

public enum Genre {
    ACTION, ADVENTURE, ANIMATION, COMEDY, CRIME, DRAMA, FANTASY, HORROR, ROMANCE, SCIFI, THRILLER, NONE;

    public static String toString(Genre g) {
        switch (g) {
            case ACTION:
                return "Action";
            case DRAMA:
                return "Drama";
            case COMEDY:
                return "Comedy";
            case ROMANCE:
                return "Romance";
            case HORROR:
                return "Horror";
            case THRILLER:
                return "Thriller";
            case ANIMATION:
                return "Animation";
            case CRIME:
                return "Crime";
            case ADVENTURE:
                return "Adventure";
            case FANTASY:
                return "Fantasy";
            case SCIFI:
                return "Sci-Fi";
            default:
                return "Unknown";
        }
    }

    public static Genre fromString(String s){
        switch (s) {
            case "Action":
                return Genre.ACTION;
            case "Animation":
                return Genre.ANIMATION;
            case "Adventure":
                return Genre.ADVENTURE;
            case "Comedy":
                return Genre.COMEDY;
            case "Crime":
                return Genre.CRIME;
            case "Drama":
                return Genre.DRAMA;
            case "Fantasy":
                return Genre.FANTASY;
            case "Horror":
                return Genre.HORROR;
            case "Romance":
                return Genre.ROMANCE;
            case "Sci-Fi":
                return Genre.SCIFI;
            case "Thriller":
                return Genre.THRILLER;
            default:
                return Genre.NONE;
        }
    }
}