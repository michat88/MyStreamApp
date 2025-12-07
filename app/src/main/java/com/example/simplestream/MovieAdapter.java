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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener;

    // Interface untuk mendeteksi klik
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        // Mencegah error jika list awal kosong
        this.movieList = (movieList != null) ? movieList : new ArrayList<>();
        this.listener = listener;
    }

    // Fungsi untuk memperbarui data dari Scraper (Internet)
    public void updateData(List<Movie> newMovies) {
        this.movieList = newMovies;
        notifyDataSetChanged(); // Beritahu tampilan ada data baru!
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

        // 1. Set Teks
        holder.textTitle.setText(movie.getTitle());
        holder.textRating.setText(movie.getRating());
        
        // 2. Logika Label Kualitas (HD/CAM)
        if (movie.getQuality().isEmpty()) {
            // Sembunyikan label jika tidak ada info kualitas
            holder.qualityContainer.setVisibility(View.GONE);
        } else {
            holder.textQuality.setText(movie.getQuality());
            holder.qualityContainer.setVisibility(View.VISIBLE);
        }

        // 3. Muat Gambar dengan Sudut Melengkung
        Glide.with(context)
                .load(movie.getPosterUrl())
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgPoster);

        // 4. Aksi Klik
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMovieClick(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // ViewHolder: Penghubung ke ID di XML
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView textTitle, textQuality, textRating;
        View qualityContainer; // Wadah label kualitas

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textQuality = itemView.findViewById(R.id.textQuality);
            textRating = itemView.findViewById(R.id.textRating);
            
            // Trik: Mengambil parent dari textQuality (yaitu CardView merah kecil)
            // Supaya bisa kita hilangkan satu kotak penuh kalau kualitas kosong
            qualityContainer = (View) textQuality.getParent();
        }
    }
}
