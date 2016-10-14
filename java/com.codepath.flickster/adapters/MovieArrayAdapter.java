package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.CommonUtils.CommonConstants;
import com.codepath.flickster.CommonUtils.PopularityStatus;
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
        //return getItem(position).getStars() >= CommonConstants.POPULAR_RATING ? CommonConstants.ONE : CommonConstants.ZERO;
        return getItem(position).getPopularity().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
        //return PopularityStatus.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        //Get the current screen orientation( Portrait-1 or Landscape-2)
        screenOrientation = parent.getResources().getConfiguration().orientation;
        PopularityStatus movieType = PopularityStatus.values()[getItemViewType(position)];
        //int movieType = getItemViewType(position);
        switch(movieType) {
            case POPULAR:
                PopularMovieViewHolder popularMovieViewHolder;
                if(convertView == null) {
                    popularMovieViewHolder = new PopularMovieViewHolder();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    popularMovieViewHolder.moviePosterHolder = (ImageView) convertView.findViewById(R.id.moviePoster);
                    popularMovieViewHolder.moviePosterHolder.setImageResource(0);
                    convertView.setTag(popularMovieViewHolder);
                } else {
                    popularMovieViewHolder = (PopularMovieViewHolder) convertView.getTag();
                }
                Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder( R.drawable.progress_animation ).into(popularMovieViewHolder.moviePosterHolder);
                break;

            case UNPOPULAR:
                UnPopularMovieViewHolder unPopularMovieViewHolder;
                if(convertView == null) {
                    unPopularMovieViewHolder = new UnPopularMovieViewHolder();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    unPopularMovieViewHolder.moviePosterHolder = (ImageView) convertView.findViewById(R.id.moviePoster);
                    unPopularMovieViewHolder.moviePosterHolder.setImageResource(0);
                    unPopularMovieViewHolder.movieTitleHolder = (TextView) convertView.findViewById(R.id.movieTitle);
                    unPopularMovieViewHolder.movieOverviewHolder = (TextView) convertView.findViewById(R.id.movieOverview);
                    convertView.setTag(unPopularMovieViewHolder);
                }else {
                    unPopularMovieViewHolder = (UnPopularMovieViewHolder) convertView.getTag();
                }
                unPopularMovieViewHolder.movieTitleHolder.setText(movie.getMovieTitle());
                unPopularMovieViewHolder.movieOverviewHolder.setText(movie.getOverview());
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    //portrait mode
                    Picasso.with(getContext()).load(movie.getPosterPath()).placeholder(R.drawable.progress_animation).into(unPopularMovieViewHolder.moviePosterHolder);
                } else if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //landscape mode
                    Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.drawable.progress_animation).into(unPopularMovieViewHolder.moviePosterHolder);
                }
                break;

        }
        return convertView;

    }

    private static class UnPopularMovieViewHolder {
        TextView movieTitleHolder;
        TextView movieOverviewHolder;
        ImageView moviePosterHolder;

    }

    private static class PopularMovieViewHolder  {
        ImageView moviePosterHolder;

    }

    private View getInflatedLayoutForType(int type) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(type == CommonConstants.ONE){
            return inflater.inflate(R.layout.item_popular_movie, null);
        }else{
            return inflater.inflate(R.layout.item_movie, null);
        }
    }

    /*abstract private static class MovieViewHolder {
        ImageView moviePosterHolder;
        public MovieViewHolder(ImageView imageView) {
            moviePosterHolder = imageView;
            moviePosterHolder.setImageResource(0);
        }
    }

    private static class UnPopularMovieViewHolder extends MovieViewHolder {
        public TextView movieTitleHolder;
        public TextView movieOverviewHolder;
        public UnPopularMovieViewHolder(ImageView imageView) {
            super(imageView);
        }
        public UnPopularMovieViewHolder setMovieTitleHolder(TextView textView) {
            movieTitleHolder = textView;
            return this;
        }

        public UnPopularMovieViewHolder setMovieOverviewHolder(TextView textView) {
            movieOverviewHolder = textView;
            return this;
        }
    }

    private static class PopularMovieViewHolder extends MovieViewHolder {
        public PopularMovieViewHolder(ImageView imageView) {
            super(imageView);
        }
    }*/
}
