package com.codepath.flickster.Models;

import com.codepath.flickster.CommonUtils.CommonConstants;
import com.codepath.flickster.CommonUtils.PopularityStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditikakadebansal on 10/11/16.
 */


public class Movie {

    private final int POPULARITY_THRESHOLD = 5;

    private String posterPath;
    private String backdropPath;
    private String movieTitle;
    private String overview;
    private int stars;

    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.movieTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.stars = jsonObject.getInt("vote_average");
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

    public PopularityStatus getPopularity() {
        if (this.stars >= POPULARITY_THRESHOLD) {
            return PopularityStatus.POPULAR;
        }
        return PopularityStatus.UNPOPULAR;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s",posterPath);
    }

    public String getBackdropPath(){
        return String.format("https://image.tmdb.org/t/p/w342%s",backdropPath);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return overview;
    }

    public int getStars(){
        return stars;
    }
}
