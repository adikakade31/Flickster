package com.codepath.flickster.Models;

import com.codepath.flickster.CommonUtils.ViewHolderType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aditikakadebansal on 10/11/16.
 */


public class Movie {

    private final float POPULARITY_THRESHOLD = 5;

    private static HashMap<Integer, Movie> idToMovieMap;

    private String posterPath;
    private String backdropPath;
    private String movieTitle;
    private String overview;
    private String releaseDate;
    private float stars;
    private int movieId;

    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.movieTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.stars = Float.valueOf(String.valueOf(jsonObject.getDouble("vote_average")));;
        this.releaseDate = jsonObject.getString("release_date");
        this.movieId = jsonObject.getInt("id");
    }

    public static ArrayList<Movie> getMoviesFromJSONArray(JSONArray jsonArray) {
        if (idToMovieMap == null)idToMovieMap = new HashMap<>();
        ArrayList<Movie> movieResults =  new ArrayList<>();
        for(int i = 0; i < jsonArray.length() ; i++){
            try {
                Movie movie = new Movie(jsonArray.getJSONObject(i));
                movieResults.add(movie);
                idToMovieMap.put(movie.getMovieId(), movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieResults;
    }

    public ViewHolderType getViewHolderType() {
        if (this.stars >= POPULARITY_THRESHOLD) {
            return ViewHolderType.WITHOUT_DETAILS;
        }
        return ViewHolderType.WITH_DETAILS;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s",posterPath);
    }

    public String getBackdropPath(){
        return String.format("https://image.tmdb.org/t/p/w342%s",backdropPath);
    }

    public static Movie getMovieForId(int movieId) {
        return idToMovieMap.get(movieId);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return overview;
    }

    public float getStars(){
        return stars;
    }

    public String getReleaseDate() { return releaseDate; }

    public int getMovieId() { return movieId; }
}
