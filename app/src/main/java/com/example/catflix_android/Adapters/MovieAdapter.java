package com.example.catflix_android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.catflix_android.Activities.MovieDetailsActivity;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView thumbnail;

        MovieViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
        }

        void bind(Movie movie, Context context) {
            // Set name
            name.setText(movie.getName() != null ? movie.getName() : "Unknown");

            // Construct the full image URL
            String url = movie.getThumbnail();
            String imageUrl = "http://10.0.2.2:8080/media/movieThumbnails/" + url;

            // Use Glide to load the image
            Glide.with(thumbnail.getContext())
                    .load(imageUrl)
                    .skipMemoryCache(true)  // Skip memory cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                    .into(thumbnail);

            // Set click listener
            itemView.setOnClickListener(v -> {
                // Navigate to MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("movie_id", movie.get_id()); // Pass the _id as an extra
                context.startActivity(intent);
            });
        }
    }

    private final LayoutInflater inflater;
    private final Context context;
    private List<Movie> movieList;

    public MovieAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movieList != null) {
            Movie current = movieList.get(position);
            holder.bind(current, context);
        }
    }

    public void setMovieResponse(List<Movie> response) {
        this.movieList = response;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (movieList != null) ? movieList.size() : 0;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
