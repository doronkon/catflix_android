package com.example.catflix_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Add Glide dependency in build.gradle
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private ImageView thumbnail;

        private MovieViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    private final LayoutInflater inflater;
    private List<Movie> movieList;

    public MovieAdapter(Context context) {
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

            // Set name and description
            holder.name.setText(current.getName() != null ? current.getName() : "Unknown");

            // Construct the full image URL
            String url = current.getThumbnail();
            String imageUrl = "http://10.0.2.2:8080/media/movieThumbnails/" + url;

            // Use Glide to load the image into the thumbnail ImageView
            Glide.with(holder.thumbnail.getContext())
                    .load(imageUrl)
                    .into(holder.thumbnail);
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
