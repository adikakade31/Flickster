package com.codepath.flickster.Activities;


import android.content.Intent;
import android.os.Bundle;

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


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by aditikakadebansal on 10/13/16.
 */
public class MovieDetailActivity extends YouTubeBaseActivity {

    public static final String YouTubeAPIKey = "AIzaSyAxnWi-q-bDtahHf1nS7XitbxAIHC1x-rwE";
    private String youTubeTrailerSource;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId",0);
        Movie movie = Movie.getMovieForId(movieId);

        setContentView(R.layout.activity_movie_detail);

        TextView movieTitle = (TextView) findViewById(R.id.detailMovieTitle);
        TextView movieOverview = (TextView)findViewById(R.id.detailMovieOverview);
        TextView movieReleaseDate = (TextView) findViewById(R.id.detailMovieReleaseDate);
        RatingBar movieRatingBar = (RatingBar) findViewById(R.id.movieRatingBar);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);

        movieTitle.setText(movie.getMovieTitle());
        movieOverview.setText(movie.getOverview());
        movieReleaseDate.setText("Release Date: "+ movie.getReleaseDate());
        movieRatingBar.setRating(movie.getStars()/2);

        youTubePlayerView.initialize(YouTubeAPIKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        //youTubePlayer.cueVideo("hM_1oO5190g");
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
