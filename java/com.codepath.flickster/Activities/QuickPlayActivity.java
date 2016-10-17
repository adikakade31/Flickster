package com.codepath.flickster.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

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
 * Created by aditikakadebansal on 10/15/16.
 */
public class QuickPlayActivity extends YouTubeBaseActivity {
    public static final String YouTubeAPIKey = "AIzaSyAxnWi-q-bDtahHf1nS7XitbxAIHC1x-rwE";
    private String youTubeTrailerSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        Intent intent = getIntent();
        final int movieId = intent.getIntExtra("movieId",0);
        youTubePlayerView.initialize(YouTubeAPIKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                        //youTubePlayer.loadVideo("hM_1oO5190g");
                        youTubePlayer.loadVideo(fetchTrailerYouTubeUrl(movieId));
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
