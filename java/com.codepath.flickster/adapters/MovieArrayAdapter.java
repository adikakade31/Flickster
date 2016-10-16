package com.codepath.flickster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.Activities.QuickPlayActivity;
import com.codepath.flickster.CommonUtils.ViewHolderType;
import com.codepath.flickster.Models.Movie;
import com.codepath.flickster.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        //super.getItemViewType(position);
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return ViewHolderType.WITH_DETAILS.ordinal();
        }
        return getItem(position).getPopularity().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return ViewHolderType.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        final int movieId = movie.getMovieId();
        //Get the current screen orientation( Portrait-1 or Landscape-2)
        screenOrientation = parent.getResources().getConfiguration().orientation;
        ViewHolderType viewHolderType = ViewHolderType.values()[getItemViewType(position)];
        switch(viewHolderType) {
            case WITHOUT_DETAILS:
                IMovieViewHolderWithoutDetails movieViewHolderWithoutDetails;
                if(convertView == null) {
                    movieViewHolderWithoutDetails = new MovieViewHolderWithoutDetails();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    movieViewHolderWithoutDetails.setMoviePosterHolder((ImageView) convertView.findViewById(R.id.moviePoster));
                    //movieViewHolderWithoutDetails.moviePosterHolder.setImageResource(0);
                    //For playing video from list automatically
                    movieViewHolderWithoutDetails.setPlayVideo((ImageButton) convertView.findViewById(R.id.play_button), position, movieId, getContext());
                    //movieViewHolderWithoutDetails.playVideo.setTag(position);
                    //Ends
                    convertView.setTag(movieViewHolderWithoutDetails);
                } else {
                    movieViewHolderWithoutDetails = (IMovieViewHolderWithoutDetails) convertView.getTag();
                }
                /*movieViewHolderWithoutDetails.playVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(view.getContext(), QuickPlayActivity.class);
                        myIntent.putExtra("movieId", movieId);
                        ((Activity)getContext()).startActivityForResult(myIntent, 0);
                    }
                });*/
                Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder( R.drawable.progress_animation ).into(movieViewHolderWithoutDetails.getMoviePosterHolder());
                break;

            case WITH_DETAILS:
                IMovieViewHolderWithDetails movieViewHolderWithDetails;
                if(convertView == null) {
                    movieViewHolderWithDetails = new MovieViewHolderWithDetails();
                    convertView = getInflatedLayoutForType(getItemViewType(position));
                    movieViewHolderWithDetails.setMoviePosterHolder((ImageView) convertView.findViewById(R.id.moviePoster));
                    //movieViewHolderWithDetails.setMoviePosterHolder(moviePosterHolder.setImageResource(0));
                    movieViewHolderWithDetails.setMovieTitleHolder((TextView) convertView.findViewById(R.id.movieTitle));
                    movieViewHolderWithDetails.setMovieOverviewHolder((TextView) convertView.findViewById(R.id.movieOverview));
                    convertView.setTag(movieViewHolderWithDetails);
                }else {
                    movieViewHolderWithDetails = (IMovieViewHolderWithDetails) convertView.getTag();
                }
                movieViewHolderWithDetails.getMovieTitleHolder().setText(movie.getMovieTitle());
                movieViewHolderWithDetails.getMovieOverviewHolder().setText(movie.getOverview());
                String movieBannerPath = null;
                if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    //portrait mode
                    movieBannerPath = movie.getPosterPath();
                    } else if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //landscape mode
                    movieBannerPath = movie.getBackdropPath();
                    }
                Picasso.with(getContext()).load(movieBannerPath).placeholder(R.drawable.progress_animation).into(movieViewHolderWithDetails.getMoviePosterHolder());
                break;

        }
        return convertView;

    }

    private static interface IMovieViewHolderWithDetails {
        public void setMoviePosterHolder(ImageView imageView);
        public void setMovieTitleHolder(TextView textview);
        public void setMovieOverviewHolder(TextView textView);
        public TextView getMovieTitleHolder();
        public TextView getMovieOverviewHolder();
        public ImageView getMoviePosterHolder();
    }
    private static interface IMovieViewHolderWithoutDetails {
        public void setMoviePosterHolder(ImageView imageView);
        public void setPlayVideo(ImageButton imageButton, int position, final int movieID, final Context context);
        public ImageView getMoviePosterHolder();
    }

    private static class MovieViewHolderWithDetails
            implements IMovieViewHolderWithDetails, IMovieViewHolderWithoutDetails {
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

        public void setPlayVideo(ImageButton imageButton, int position, final int movieID,  final Context context) {}
    }

    private static class MovieViewHolderWithoutDetails
            implements IMovieViewHolderWithDetails, IMovieViewHolderWithoutDetails {
        ImageView moviePosterHolder;
        ImageButton playVideo;
        public void setMoviePosterHolder(ImageView imageView) {
            moviePosterHolder = imageView;
            moviePosterHolder.setImageResource(0);
        }

        public TextView getMovieTitleHolder() {
            return null;
        }
        public TextView getMovieOverviewHolder() {
            return null;
        }

        public ImageView getMoviePosterHolder() {
            return moviePosterHolder;
        }

        public void setMovieTitleHolder(TextView textview) {

        }
        public void setMovieOverviewHolder(TextView textView) {


        }
        public void setPlayVideo(ImageButton imageButton, int position, final int movieId1, final Context context) {
            playVideo = imageButton;
            playVideo.setTag(position);
            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), QuickPlayActivity.class);
                    myIntent.putExtra("movieId", movieId1);
                    ((Activity)context).startActivityForResult(myIntent, 0);
                }
            });
        }
    }

    private View getInflatedLayoutForType(int type) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(type == ViewHolderType.WITHOUT_DETAILS.ordinal()){
            return inflater.inflate(R.layout.item_popular_movie, null);
        }else{
            return inflater.inflate(R.layout.item_movie, null);
        }
    }

}
