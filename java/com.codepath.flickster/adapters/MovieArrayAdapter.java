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
    private static class ViewHolder{
        ImageView moviePosterHolder;
        TextView movieTitleHolder;
        TextView movieOverviewHolder;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.moviePosterHolder = (ImageView) convertView.findViewById(R.id.moviePoster);
            viewHolder.moviePosterHolder.setImageResource(0);
            viewHolder.movieTitleHolder = (TextView) convertView.findViewById(R.id.movieTitle);
            viewHolder.movieOverviewHolder = (TextView) convertView.findViewById(R.id.movieOverview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(getContext()).load(movie.getPosterPath()).into(viewHolder.moviePosterHolder);
        viewHolder.movieTitleHolder.setText(movie.getMovieTitle());
        viewHolder.movieOverviewHolder.setText(movie.getOverview());

        return convertView;

    }
}
