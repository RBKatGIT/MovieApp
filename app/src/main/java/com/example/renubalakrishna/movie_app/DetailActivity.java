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

        // Get the intent that started this activity
        Intent intent = getIntent();

        Bundle params = intent.getExtras();

        if(params != null)
        {
            // Display title
            String title = params.getString("TITLE");
            if(title!= null){
                TextView titleView = (TextView) findViewById(R.id.detail_movie_title);
                titleView.setText(title);
            }

            // Display poster
            String poster_url = params.getString("POSTER_URL");
            if(poster_url!= null){
                ImageView imagePoster = (ImageView) findViewById(R.id.detail_image_poster);
                with(getApplicationContext()).load(poster_url).into(imagePoster);
            }

            // Display moview overview
            String synopsis = params.getString("PLOT_SYNOPSIS");
            if(synopsis!= null){
                TextView synopsisView = (TextView) findViewById(R.id.detail_movie_synopsis);
                synopsisView.setText("Plot Synopsis : "+ synopsis);
            }

            // Display rating
            Double rating = params.getDouble("USER_RATING", 0.0);
            TextView ratingView = (TextView) findViewById(R.id.detail_user_rating);
            ratingView.setText("Rating : "+rating);

            // Display release Date
            String release_date = params.getString("RELEASE_DATE");
            if(release_date!= null){
                TextView releaseView = (TextView) findViewById(R.id.detail_release_date);
                releaseView.setText("Released on : "+release_date);
            }
        }

    }
}
