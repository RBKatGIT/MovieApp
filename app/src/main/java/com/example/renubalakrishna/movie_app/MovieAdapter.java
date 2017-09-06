package com.example.renubalakrishna.movie_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import static com.squareup.picasso.Picasso.with;

// File:         MovieAdapter.java
// Created:      [2017/08/05 creation date]
// Last Changed: $Date: 2017/08/05 15:15:25 $
// Author:       <A HREF="mailto:renubk@gmail.com">[Name]</A>
//
class MovieAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Movie> mMovieList;

    public MovieAdapter(Context mContext, ArrayList<Movie> movieList) {
        this.mContext = mContext;
        this.mMovieList = movieList;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Movie getItem(int position) {
        if(mMovieList == null || position >= mMovieList.size() ) {
            return null;
        }
        else
        {
            return mMovieList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mMovieList == null){
            return null;
        }
        final String posterUrl = mMovieList.get(position).getPosterUrl();
        Log.i("getView", "Image URL is" + posterUrl);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.linearlayout_movies,parent,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview_poster);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        with(mContext).load(posterUrl).into(viewHolder.imageView);
        return convertView;
    }

    public void setMovieList(ArrayList<Movie> movieList){
        if(movieList == null){
            this.mMovieList.clear();
        }
        else{
            this.mMovieList = movieList;
        }
    }

    public static class ViewHolder{
        public ImageView imageView;
    }
}
