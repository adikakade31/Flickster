package com.codepath.flickster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aditikakadebansal on 10/11/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
        }
        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.moviePoster);
        moviePoster.setImageResource(0);
        Picasso.with(getContext()).load(movie.getPosterPath()).into(moviePoster);
        TextView movieTitle = (TextView) convertView.findViewById(R.id.movieTitle);
        movieTitle.setText(movie.getMovieTitle());
        TextView movieOverview = (TextView) convertView.findViewById(R.id.movieOverview);
        movieOverview.setText(movie.getOverview());

        return convertView;

    }
}
