package com.example.renubalakrishna.movie_app;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// File:         RecyclerViewAdapter.java
// Created:      [2017/08/06 creation date]
// Last Changed: $Date: 2017/08/06 15:15:25 $
// Author:       <A HREF="mailto:renubk@gmail.com">[Renu Balakrishna]</A>
//

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RViewHolder> {

    // Member variable for the date
    private ArrayList<Movie> mMovieList;

    // Member variable for the context
    private final Context mContext;

    // For testing, static count
    // TODO : remove after testing
    private static int mCount=0;

    private final GridItemClickListener mOnClickListener;

    // This interface can be implemented by MAIn activity for wht to do on an item click
    public interface GridItemClickListener{
        void onGridItemClick(int itemPosition);
    }

    // Constructor , pass in the data
    public RecyclerViewAdapter(Context context, ArrayList<Movie> movieList, GridItemClickListener clickListener){
        mContext = context;
        mMovieList = movieList;
        mOnClickListener = clickListener;

    }

    public class RViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Member variables for all the views
        public final ImageView mPosterView;//

        // Constructor that gets the entire item and do the look up for views
        // inside it
        public RViewHolder(View itemView){
            super(itemView);
            mPosterView = (ImageView) itemView.findViewById(R.id.imageview_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get clicked item position
            int position = getAdapterPosition();
            mOnClickListener.onGridItemClick(position);
        }
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Increment holder count
        mCount++;
        Log.i("onCreateViewHolder", "Count : "+mCount);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a view and inflate it with the item layout
        View inflatedView = inflater.inflate(R.layout.linearlayout_movies, parent, false);

        // Create a view holder with this View
        return new RViewHolder(inflatedView);
    }

    // Populate data in the item through holder
    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        Log.i("onBindViewHolder", "Count : "+mCount);
        if(position<mMovieList.size()) {
            Movie movie = mMovieList.get(position);
            String posterUrl = movie.getPosterUrl();
            Picasso.with(mContext).load(posterUrl).placeholder(ContextCompat.getDrawable(mContext,R.drawable.ic_launcher)).into(holder.mPosterView);
            Log.i("onBindViewHolder", "Count : "+mCount+"Picasso load request done");
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovieList(ArrayList<Movie> movieList){
        if(movieList == null)
        {
            mMovieList.clear();
        }
        else
        {
            mMovieList= movieList;
        }
    }

    public Movie getItem(int position){
        if(position<mMovieList.size()){
            return mMovieList.get(position);
        }
        else{
            return null;
        }
    }
}
