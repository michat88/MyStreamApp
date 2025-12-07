package com.example.simplestream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener;

    // Interface agar MainActivity tahu kalau ada film diklik
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // 1. Set Judul
        holder.textTitle.setText(movie.getTitle());

        // 2. Muat Gambar Poster dari Internet pakai Glide
        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(android.R.drawable.ic_menu_gallery) // Gambar sementara loading
                .error(android.R.drawable.stat_notify_error) // Gambar kalau error
                .into(holder.imgPoster);

        // 3. Aksi Klik
        holder.itemView.setOnClickListener(v -> {
            listener.onMovieClick(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // Kelas Penampung Komponen (ViewHolder)
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView textTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}
