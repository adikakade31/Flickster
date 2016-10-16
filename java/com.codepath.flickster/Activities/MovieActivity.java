package com.codepath.flickster.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    private ArrayList<Movie> movies;
    private MovieArrayAdapter movieArrayAdapter;
    private ListView moviesViewer;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        moviesViewer = (ListView) findViewById(R.id.moviesViewer);
        moviesViewer.setItemsCanFocus(true);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        moviesViewer.setAdapter(movieArrayAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        fetchMoviesList();
        moviesViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent( view.getContext(), MovieDetailActivity.class);
                Movie selectedMovie = (Movie)parent.getAdapter().getItem(position);
                myIntent.putExtra("movieTitle", selectedMovie.getMovieTitle());
                myIntent.putExtra("movieOverview", selectedMovie.getOverview());
                myIntent.putExtra("movieBackdrop", selectedMovie.getBackdropPath());
                myIntent.putExtra("movieReleaseDate", selectedMovie.getReleaseDate());
                myIntent.putExtra("movieRating", selectedMovie.getStars());
                myIntent.putExtra("movieId", selectedMovie.getMovieId());
                startActivityForResult(myIntent, 0);
            }
        });
        //To check if video can be played by clicking of list view item
        /*moviesViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent( view.getContext(), QuickPlayActivity.class);
                Movie selectedMovie = (Movie)parent.getAdapter().getItem(position);
                myIntent.putExtra("movieId", selectedMovie.getMovieId());
                startActivityForResult(myIntent, 0);
            }
        });*/

        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesList();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchMoviesList() {
        AsyncHttpClient client = new AsyncHttpClient();
        String moviesUrl = getResources().getString(R.string.movies_url);
        client.get(moviesUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    movieArrayAdapter.clear();
                    movies.addAll(Movie.getMoviesFromJSONArray(response.getJSONArray(getResources().getString(R.string.results))));
                    movieArrayAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    Log.d("DEBUG", movies.toString());
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
