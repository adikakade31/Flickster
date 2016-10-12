package com.codepath.flickster.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    ListView moviesViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        moviesViewer = (ListView) findViewById(R.id.moviesViewer);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        moviesViewer.setAdapter(movieArrayAdapter);
        String moviesUrl = getResources().getString(R.string.movies_url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(moviesUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //JSONArray movieJsonResults = null;
                try {
                    //movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.getMoviesFromJSONArray(response.getJSONArray(getResources().getString(R.string.results))));
                    movieArrayAdapter.notifyDataSetChanged();
                    Log.d("DEBUG",movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
