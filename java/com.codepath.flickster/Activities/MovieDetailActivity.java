package com.codepath.flickster.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by aditikakadebansal on 10/13/16.
 */
public class MovieDetailActivity extends YouTubeBaseActivity {

    public static final String YouTubeAPIKey = "AIzaSyAxnWi-q-bDtahHf1nS7XitbxAIHC1x-rwE";
    private String youTubeTrailerSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        TextView movieTitle = (TextView) findViewById(R.id.detailMovieTitle);
        TextView movieOverview = (TextView)findViewById(R.id.detailMovieOverview);
        TextView movieReleaseDate = (TextView) findViewById(R.id.detailMovieReleaseDate);
        //ImageView movieBackdrop = (ImageView) findViewById(R.id.detailMoviePoster);
        RatingBar movieRatingBar = (RatingBar) findViewById(R.id.movieRatingBar);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        Intent intent = getIntent();
        movieTitle.setText(intent.getStringExtra("movieTitle"));
        movieOverview.setText(intent.getStringExtra("movieOverview"));
        movieReleaseDate.setText("Release Date: "+intent.getStringExtra("movieReleaseDate"));
        //Picasso.with(getApplicationContext()).load(intent.getStringExtra("movieBackdrop")).placeholder( R.drawable.progress_animation ).into(movieBackdrop);
        movieRatingBar.setRating(intent.getFloatExtra("movieRating", 0)/2);
        final int movieId = intent.getIntExtra("movieId",0);
        youTubePlayerView.initialize(YouTubeAPIKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        //youTubePlayer.cueVideo("5xVh-7ywKpE");
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        youTubePlayer.cueVideo(fetchTrailerYouTubeUrl(movieId));
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
    private String fetchTrailerYouTubeUrl(int movieId){
        AsyncHttpClient client = new AsyncHttpClient();
        String trailerURLPrefix = getResources().getString(R.string.trailer_url_prefix);
        String trailerURLSuffix = getResources().getString(R.string.trailer_url_suffix);
        String trailerURL = trailerURLPrefix + String.valueOf(movieId) + trailerURLSuffix;
        client.get(trailerURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    JSONObject jsonObject= response.getJSONArray(getResources().getString(R.string.youtube)).getJSONObject(0);
                    youTubeTrailerSource =  jsonObject.getString("source");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        return youTubeTrailerSource;

    }
}
