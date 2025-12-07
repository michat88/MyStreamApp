package com.example.simplestream;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    VideoView videoPlayer;
    TextView textStatus;
    RecyclerView rvMovies;
    MovieAdapter adapter;
    List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi Tampilan
        videoPlayer = findViewById(R.id.videoView);
        textStatus = findViewById(R.id.textStatus);
        rvMovies = findViewById(R.id.rvMovies);

        // 2. Setup Pemutar Video
        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(videoPlayer);

        // 3. Setup Daftar Film (Grid 2 Kolom)
        // Angka '2' berarti ada 2 poster berjejer ke samping
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));

        // 4. Ambil Data Film (Simulasi Provider)
        movieList = getAdicinemaxMovies();

        // 5. Pasang Adapter
        adapter = new MovieAdapter(this, movieList, new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                // Aksi saat poster diklik
                playMovie(movie);
            }
        });
        rvMovies.setAdapter(adapter);
    }

    private void playMovie(Movie movie) {
        try {
            // Ubah teks status dan mulai video
            textStatus.setText("Sedang Memutar: " + movie.getTitle());
            
            Uri videoUri = Uri.parse(movie.getVideoUrl());
            videoPlayer.setVideoURI(videoUri);
            videoPlayer.start();
            
            Toast.makeText(this, "Memuat: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Gagal memutar video", Toast.LENGTH_SHORT).show();
        }
    }

    // --- DATA DUMMY (PROVIDER TERTANAM) ---
    // Di sinilah kita memasukkan link film dan gambar poster secara manual
    private List<Movie> getAdicinemaxMovies() {
        List<Movie> movies = new ArrayList<>();

        // Film 1: Big Buck Bunny
        movies.add(new Movie(
            "Big Buck Bunny",
            "https://upload.wikimedia.org/wikipedia/commons/c/c5/Big_buck_bunny_poster_big.jpg", // Poster
            "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8" // Video
        ));

        // Film 2: Sintel
        movies.add(new Movie(
            "Sintel",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/800px-Sintel_poster.jpg",
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
        ));

        // Film 3: Tears of Steel
        movies.add(new Movie(
            "Tears of Steel",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Tears_of_Steel_poster.jpg/800px-Tears_of_Steel_poster.jpg",
            "https://content.jwplatform.com/manifests/yp34SRmf.m3u8"
        ));

        // Film 4: Elephant Dream (Contoh Tambahan)
        movies.add(new Movie(
            "Elephants Dream",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Elephants_Dream_poster.jpg/800px-Elephants_Dream_poster.jpg",
            "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8" // Pakai link tes yang sama utk contoh
        ));

        // Kamu bisa copy-paste baris di atas untuk menambah film sebanyak-banyaknya
        
        return movies;
    }
}
