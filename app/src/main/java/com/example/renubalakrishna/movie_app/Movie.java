package com.example.renubalakrishna.movie_app;

/**
 * Created by renubalakrishna on 01/09/17.
 */

public class Movie {
    // Movie title
    private String title;

    // Poster URL
    private String posterUrl;

    // Plot synopsis
    private String ploSynopsis;

    // User rating
    private double userRating;

    // Release Date
    private String releaseDate;

    // Currently movie needs to initialize all members
    public Movie(String title, String posterUrl, String ploSynopsis, double userRating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.ploSynopsis = ploSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    //  Getters for all data
    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPloSynopsis() {
        return ploSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
