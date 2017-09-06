package com.example.renubalakrishna.movie_app;

// File:         Movie.java
// Created:      [2017/08/05 creation date]
// Last Changed: $Date: 2017/08/05 15:15:25 $
// Author:       <A HREF="mailto:renubk@gmail.com">[Name]</A>
//

public class Movie {
    // Movie title
    private final String title;

    // Poster URL
    private final String posterUrl;

    // Plot synopsis
    private final String ploSynopsis;

    // User rating
    private final double userRating;

    // Release Date
    private final String releaseDate;

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
