package com.example.renubalakrishna.movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import static com.squareup.picasso.Picasso.with;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        Movie params = intent.getParcelableExtra("DATA_KEY");

        if(params != null)
        {
            // Display title
            String title = params.getTitle();
            if(title!= null){
                TextView titleView = (TextView) findViewById(R.id.detail_movie_title);
                titleView.setText(title);
            }

            // Display poster
            String poster_url = params.getPosterUrl();
            if(poster_url!= null){
                ImageView imagePoster = (ImageView) findViewById(R.id.detail_image_poster);
                with(getApplicationContext()).load(poster_url).into(imagePoster);
            }

            // Display movie overview
            String synopsis = params.getPloSynopsis();
            if(synopsis!= null){
                TextView synopsisView = (TextView) findViewById(R.id.detail_movie_synopsis);
                synopsis = getString(R.string.plot_synopsis) +synopsis;
                synopsisView.setText(synopsis);
            }

            // Display rating
            Double rating = params.getUserRating();
            TextView ratingView = (TextView) findViewById(R.id.detail_user_rating);
            String ratingString = getString(R.string.rating)+rating;
            ratingView.setText(ratingString);

            // Display release Date
            String release_date = params.getReleaseDate();
            if(release_date!= null){
                TextView releaseView = (TextView) findViewById(R.id.detail_release_date);
                release_date = getString(R.string.release_on)+release_date;
                releaseView.setText(release_date);
            }
        }

    }
}
