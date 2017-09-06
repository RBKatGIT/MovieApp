package com.example.renubalakrishna.movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    public enum SortPreference{
        POPULARITY,
        TOP_RATED
    }
    // Instantiate the request queue
    private RequestQueue mRequestQueue;

    private MovieAdapter mAdapter;

    // Network error status
    private boolean mNetworkError= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner initialization
        Spinner mPreferenceSpinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.preferences, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mPreferenceSpinner.setAdapter(adapter);
        mPreferenceSpinner.setOnItemSelectedListener(MainActivity.this);

        // Get the gridView from layout
        GridView gridView =   (GridView) findViewById(R.id.grid_view);
        ArrayList<Movie> emptyArray = new ArrayList<>();
        // Create a new adapter object from the adapter class
        mAdapter = new MovieAdapter(this, emptyArray);
        Log.i("onCreate", "Starting query");
        mRequestQueue = Volley.newRequestQueue(this);

        // Set adapter for the grid View
        gridView.setAdapter(mAdapter);

        // Load the movie posters by popularity
        startQuery(SortPreference.POPULARITY);

        // On click show detailed Page
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mAdapter.getItem(position);
                if(movie != null) {
                    Log.i("Onclick", "Sending to Detail Activity");
                    Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle params = new Bundle();
                    params.putString("TITLE", movie.getTitle());
                    params.putString("POSTER_URL", movie.getPosterUrl());
                    params.putString("PLOT_SYNOPSIS", movie.getPloSynopsis());
                    params.putDouble("USER_RATING", movie.getUserRating());
                    params.putString("RELEASE_DATE", movie.getReleaseDate());
                    detailIntent.putExtras(params);
                    startActivity(detailIntent);
                }
                else {
                    Log.e("Onclick", "Empty movie item");
                }
            }
        });
    }


    /**
     * Clear movie list
     */
    private void clearGridView(){
        if(mAdapter != null) {
            mAdapter.setMovieList(null);
            setGridViewVisible();
            mAdapter.notifyDataSetChanged();
        }
        else
        {
            Log.e("clearGridView", "Null Adapter Error");
        }
    }

    /**
     * Handle web query for the movie list
     * @param preference - By popularity or top rating
     */
    private void startQuery(SortPreference preference){
        String url = null;
        if(preference == SortPreference.POPULARITY) {
            Log.i("startQuery", "Start query based on popularity");
            // URL for popular movies
            url = "https://api.themoviedb.org/3/discover/movie?api_key=" + (getString(R.string.api_key)) + "&language=en-US&sort_by=popularity.desc";
        }
        else if(preference == SortPreference.TOP_RATED){
            Log.i("startQuery", "Start query based top-rating");
            // URL for popular movies
            url = "https://api.themoviedb.org/3/discover/movie?api_key=" + (getString(R.string.api_key)) + "&language=en-US&sort_by=vote_average.desc";
        }

        JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET,url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            ArrayList<Movie> movieList = new ArrayList<>();
                            // Process each entry in the JSON response
                            for(int i =0; i<jsonArray.length(); i++){
                                JSONObject movieJson = jsonArray.getJSONObject(i);
                                // Extract movie data
                                String title = movieJson.getString("title");
                                String poster_path = movieJson.getString("poster_path");
                                String poster_url = getString(R.string.poster_base_url)+getString(R.string.poster_size)+poster_path;
                                String plotSynopsis = movieJson.getString("overview");
                                double userRating = movieJson.getDouble("vote_average");
                                String releaseDate = movieJson.getString("release_date");
                                // Skip the movies with empty fields, this was added since some of
                                // the movies did not have a poster path
                                if(!poster_path.equals("null") && !poster_url.equals("null") && !releaseDate.equals("null")) {
                                    Movie movie = new Movie(title, poster_url, plotSynopsis, userRating, releaseDate);
                                    movieList.add(movie);
                                    Log.i("onResponse", "Adding Movie: " + title);
                                }
                            }
                            mAdapter.setMovieList(movieList);
                            // Make sure Grid view is enabled
                            setGridViewVisible();
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.i("startQuery", "JSONException");
                            e.printStackTrace();
                            // Display network error on screen
                            setErrorViewVisible();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.i("startQuery", "Query error, see log below");
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jReq);
    }

    /**
     * Network error occurred
     * Keep status to swap the views later when fixed
     */
    private void setErrorViewVisible(){
        mNetworkError = true;
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        TextView errorView = (TextView) findViewById(R.id.error_text);
        // Show error on screen
        gridView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    /**
     * Network error Fixed
     * Re-enable grid view
     */
    private void setGridViewVisible(){
        if(mNetworkError) {
            mNetworkError = false;
            GridView gridView = (GridView) findViewById(R.id.grid_view);
            TextView errorView = (TextView) findViewById(R.id.error_text);
            // Enable grid
            gridView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        // Retrieve the selected item
        String item = parent.getItemAtPosition(pos).toString();
        if(item.equals("TOP RATED")){
            clearGridView();
            SortPreference preference = SortPreference.TOP_RATED;
            startQuery(preference);
        }
        else{
            clearGridView();
            SortPreference preference = SortPreference.POPULARITY;
            startQuery(preference);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.i("onNothingSelected", "Nothing selected");
    }

}
