package com.codepath.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.flixster.DetailActivity;
import com.codepath.flixster.R;
import com.codepath.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MovieAdapter";
    private static final int HIGH_RATE = 0, LOW_RATE = 1;
    private static final int RATE_THRESHOLD = 7;

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getRate() >= RATE_THRESHOLD) {
            return HIGH_RATE;
        } else {
            return LOW_RATE;
        }
    }

    // inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (displayBanner(viewType)) {
            View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie_best, parent, false);
            return new ViewHolderBest(movieView);
        } else {
            View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            return new ViewHolder(movieView);
        }
    }

    // populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        if (displayBanner(holder.getItemViewType())) {
            ((ViewHolderBest)holder).bind(movie);
        } else {
            ((ViewHolder)holder).bind(movie);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private boolean displayBanner(int viewType) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                && viewType == HIGH_RATE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }
            Glide.with(context).load(imageUrl).into(ivPoster);

            // Register click listener on whole row
            // Navigate to a new activity
            container.setOnClickListener(v -> {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("movie", Parcels.wrap(movie));
                Pair<View, String> p1 = Pair.create(tvTitle, "title");
                Pair<View, String> p2 = Pair.create(tvOverview, "overview");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, p1, p2);
                context.startActivity(i, options.toBundle());
            });
        }
    }

    public class ViewHolderBest extends RecyclerView.ViewHolder {

        RelativeLayout container;
        ImageView ivPoster;

        public ViewHolderBest(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).into(ivPoster);
            container.setOnClickListener(v -> {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("movie", Parcels.wrap(movie));
                context.startActivity(i);
            });
        }
    }
}
