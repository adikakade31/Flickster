package com.codepath.flickster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.Activities.MovieDetailActivity;
import com.codepath.flickster.Activities.QuickPlayActivity;
import com.codepath.flickster.CommonUtils.ViewHolderType;
import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by aditikakadebansal on 10/11/16.
 */


public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private int screenOrientation;


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewHolderType().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return ViewHolderType.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int movieId = getItem(position).getMovieId();
        //Get the current screen orientation( Portrait-1 or Landscape-2)
        screenOrientation = parent.getResources().getConfiguration().orientation;
        ViewHolderType viewHolderType = ViewHolderType.values()[getItemViewType(position)];
        switch(viewHolderType) {
            case WITHOUT_DETAILS:
                MovieViewHolderWithoutDetails movieViewHolderWithoutDetails;
                if(convertView == null) {
                    movieViewHolderWithoutDetails = new MovieViewHolderWithoutDetails();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    movieViewHolderWithoutDetails.setMoviePosterHolder(
                            (ImageView) convertView.findViewById(R.id.moviePoster)
                    );
                    movieViewHolderWithoutDetails.setPlayVideo(
                            (ImageButton) convertView.findViewById(R.id.play_button),
                            position
                    );
                    movieViewHolderWithoutDetails.setShowDetails(
                            (ImageButton) convertView.findViewById(R.id.show_details),
                            position
                    );
                    convertView.setTag(movieViewHolderWithoutDetails);
                } else {
                    movieViewHolderWithoutDetails =
                            (MovieViewHolderWithoutDetails) convertView.getTag();
                }
                movieViewHolderWithoutDetails.getPlayVideo().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), QuickPlayActivity.class);
                        myIntent.putExtra("movieId", movieId);
                        ((Activity)getContext()).startActivityForResult(myIntent, 0);
                    }
                });
                movieViewHolderWithoutDetails.getShowDetails().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), MovieDetailActivity.class);
                        myIntent.putExtra("movieId", movieId);
                        ((Activity)getContext()).startActivityForResult(myIntent, 0);
                    }
                });
                Picasso
                        .with(getContext())
                        .load(Movie.getMovieForId(movieId).getBackdropPath())
                        .placeholder(R.drawable.progress_animation)
                        .into(movieViewHolderWithoutDetails.getMoviePosterHolder());
                break;

            case WITH_DETAILS:
                MovieViewHolderWithDetails movieViewHolderWithDetails;
                if(convertView == null) {
                    movieViewHolderWithDetails = new MovieViewHolderWithDetails();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    movieViewHolderWithDetails.setMoviePosterHolder(
                            (ImageView) convertView.findViewById(R.id.moviePoster)
                    );
                    movieViewHolderWithDetails.setMovieTitleHolder(
                            (TextView) convertView.findViewById(R.id.movieTitle)
                    );
                    movieViewHolderWithDetails.setMovieOverviewHolder(
                            (TextView) convertView.findViewById(R.id.movieOverview)
                    );
                    convertView.setTag(movieViewHolderWithDetails);
                } else {
                    movieViewHolderWithDetails = (MovieViewHolderWithDetails) convertView.getTag();
                }
                movieViewHolderWithDetails.getMovieTitleHolder().setText(Movie.getMovieForId(movieId).getMovieTitle());
                movieViewHolderWithDetails.getMovieOverviewHolder().setText(Movie.getMovieForId(movieId).getOverview());
                String movieBannerPath = null;
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    movieBannerPath = Movie.getMovieForId(movieId).getPosterPath();
                } else if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    movieBannerPath = Movie.getMovieForId(movieId).getBackdropPath();
                }
                Picasso
                        .with(getContext())
                        .load(movieBannerPath)
                        .placeholder(R.drawable.progress_animation)
                        .into(movieViewHolderWithDetails.getMoviePosterHolder());
                break;
        }
        return convertView;
    }


    private static class MovieViewHolderWithDetails {
        TextView movieTitleHolder;
        TextView movieOverviewHolder;
        ImageView moviePosterHolder;
        public void setMoviePosterHolder(ImageView imageView) {
            moviePosterHolder = imageView;
            moviePosterHolder.setImageResource(0);
        }
        public void setMovieTitleHolder(TextView textview) {
            movieTitleHolder = textview;
        }
        public void setMovieOverviewHolder(TextView textView) {
            movieOverviewHolder = textView;
        }
        public ImageView getMoviePosterHolder() {
            return moviePosterHolder;
        }
        public TextView getMovieTitleHolder() {
            return movieTitleHolder;
        }
        public TextView getMovieOverviewHolder() {
            return movieOverviewHolder;
        }
    }

    private static class MovieViewHolderWithoutDetails {
        ImageView moviePosterHolder;
        ImageButton playVideo;
        ImageButton showDetails;
        public void setMoviePosterHolder(ImageView imageView) {
            moviePosterHolder = imageView;
            moviePosterHolder.setImageResource(0);
        }
        public ImageView getMoviePosterHolder() {
            return moviePosterHolder;
        }
        public void setPlayVideo(ImageButton imageButton, int position) {
            playVideo = imageButton;
           // playVideo.setTag(position);
        }

        public ImageButton getPlayVideo() {
            return playVideo;
        }

        public ImageButton getShowDetails() {
            return showDetails;
        }

        public void setShowDetails(ImageButton imageButton, int position) {
            showDetails = imageButton;
           // showDetails.setTag(position);
        }
    }
    private View getInflatedLayoutForType(int type) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(type == ViewHolderType.WITHOUT_DETAILS.ordinal()){
            return inflater.inflate(R.layout.item_popular_movie, null);
        } else {
            return inflater.inflate(R.layout.item_movie, null);
        }
    }
}
