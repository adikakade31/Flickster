package com.codepath.flickster.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditikakadebansal on 10/11/16.
 */
public class Movie {
    private String posterPath;
    private String movieTitle;
    private String overview;

    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = jsonObject.getString("poster_path");
        this.movieTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
    }

    public static ArrayList<Movie> getMoviesFromJSONArray(JSONArray jsonArray){
        ArrayList<Movie> movieResults =  new ArrayList<>();
        for(int i = 0; i < jsonArray.length() ; i++){
            try {
                movieResults.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieResults;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s",posterPath);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return overview;
    }
}
