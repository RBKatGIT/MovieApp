package com.example.renubalakrishna.movie_app;

// File:         Movie.java
// Created:      [2017/08/05 creation date]
// Last Changed: $Date: 2017/08/05 15:15:25 $
// Author:       <A HREF="mailto:renubk@gmail.com">[Renu Balakrishna]</A>
//

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    // Movie title
    private String mTitle;

    // Poster URL
    private String mPosterUrl;

    // Plot synopsis
    private String mPloSynopsis;

    // User rating
    private double mUserRating;

    // Release Date
    private String mReleaseDate;

    // Currently movie needs to initialize all members
    private Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mPosterUrl = in.readString();
        this.mPloSynopsis = in.readString();
        this.mUserRating = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    public Movie(){

    }

    //  Getters for all data
    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getPloSynopsis() {
        return mPloSynopsis;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public void setPloSynopsis(String mPloSynopsis) {
        this.mPloSynopsis = mPloSynopsis;
    }

    public void setUserRating(double mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPosterUrl);
        parcel.writeString(mPloSynopsis);
        parcel.writeDouble(mUserRating);
        parcel.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
