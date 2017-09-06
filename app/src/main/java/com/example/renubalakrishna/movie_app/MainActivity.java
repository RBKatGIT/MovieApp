package com.example.renubalakrishna.movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, RecyclerViewAdapter.GridItemClickListener{

    public enum SortPreference{
        POPULARITY,
        TOP_RATED
    }
    // Instantiate the request queue
    private RequestQueue mRequestQueue;

    private RecyclerViewAdapter mAdapter;

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

        // Get the recycler View
        RecyclerView rvView = (RecyclerView) findViewById(R.id.rvPosters);

        ArrayList<Movie> emptyArray = new ArrayList<>();
        // Create a new adapter object from the adapter class
        mAdapter = new RecyclerViewAdapter(this, emptyArray, this);
        // Get first set of data

        Log.i("onCreate", "Starting query");
        mRequestQueue = Volley.newRequestQueue(this);
        // Load the movie posters by popularity
        startQuery(SortPreference.POPULARITY);

        // Set adapter for the grid View
        rvView.setAdapter(mAdapter);

        //Set Layout manager
        rvView.setLayoutManager(new GridLayoutManager(this,3));

        rvView.setHasFixedSize(true);
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
        String API = BuildConfig.API_KEY;
        if(preference == SortPreference.POPULARITY) {
            Log.i("startQuery", "Start query based on popularity");
            // URL for popular movies
            url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API + "&language=en-US&page=1";
        }
        else if(preference == SortPreference.TOP_RATED){
            Log.i("startQuery", "Start query based top-rating");
            // URL for popular movies
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API + "&language=en-US&page=1";
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
                                    Movie movie = new Movie();
                                    movie.setTitle(title);
                                    movie.setPosterUrl(poster_url);
                                    movie.setPloSynopsis(plotSynopsis);
                                    movie.setUserRating(userRating);
                                    movie.setReleaseDate(releaseDate);
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
        RecyclerView rvView = (RecyclerView) findViewById(R.id.rvPosters);
        TextView errorView = (TextView) findViewById(R.id.error_text);
        // Show error on screen
        rvView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    /**
     * Network error Fixed
     * Re-enable grid view
     */
    private void setGridViewVisible(){
        if(mNetworkError) {
            mNetworkError = false;
            RecyclerView rvView = (RecyclerView) findViewById(R.id.rvPosters);
            TextView errorView = (TextView) findViewById(R.id.error_text);
            // Enable RV
            rvView.setVisibility(View.VISIBLE);
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

    @Override
    public void onGridItemClick(int itemPosition) {
        Movie movie = mAdapter.getItem(itemPosition);
        if(movie != null) {
            Log.i("onGridItemClick", "Sending to Detail Activity");
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra("DATA_KEY", movie);
            startActivity(detailIntent);
        }
        else {
            Log.e("onGridItemClick", "Empty movie item");
        }
    }
}
