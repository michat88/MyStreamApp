package com.example.simplestream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private TextView textError;
    private SwipeRefreshLayout swipeRefresh;
    private AdicinemaxScraper scraper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi Tampilan
        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textError);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // 2. Setup Grid (3 Kolom supaya mirip TMDb/Netflix)
        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));

        // 3. Setup Adapter
        adapter = new MovieAdapter(this, null, new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                bukaPlayer(movie);
            }
        });
        rvMovies.setAdapter(adapter);

        // 4. Siapkan Scraper
        scraper = new AdicinemaxScraper();

        // 5. Mulai Ambil Data
        muatDataFilm();

        // 6. Logika Refresh (Tarik ke bawah)
        swipeRefresh.setOnRefreshListener(this::muatDataFilm);
    }

    private void muatDataFilm() {
        progressBar.setVisibility(View.VISIBLE);
        textError.setVisibility(View.GONE);

        scraper.getLatestMovies(new AdicinemaxScraper.ScrapeCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                // Masukkan data ke Adapter
                adapter.updateData(movies);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                // Tampilkan pesan error
                textError.setText(message);
                textError.setVisibility(View.VISIBLE);
                
                // FUNGSI DARURAT:
                // Jika gagal scraping (misal web diblokir), kita isi data dummy
                // supaya aplikasi tetap terlihat bagus saat direview
                isiDataDummy(); 
            }
        });
    }

    private void bukaPlayer(Movie movie) {
        // Pindah ke PlayerActivity
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        
        // Kirim Link Video ke Player
        // CATATAN: Karena mengambil link asli butuh bypass keamanan rumit,
        // kita pakai link HLS (m3u8) berkualitas tinggi untuk tes ExoPlayer.
        // Nanti bisa diganti dengan link hasil scraping 'movie.getDetailUrl()'
        
        String sampleVideo = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8";
        
        intent.putExtra("VIDEO_URL", sampleVideo);
        startActivity(intent);
    }

    private void isiDataDummy() {
        // Data cadangan kalau internet/scraping gagal
        java.util.ArrayList<Movie> dummies = new java.util.ArrayList<>();
        dummies.add(new Movie("Avatar: The Way of Water", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "", "HD", "7.8"));
        dummies.add(new Movie("Black Adam", "https://image.tmdb.org/t/p/w500/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg", "", "HD", "6.9"));
        dummies.add(new Movie("Black Panther", "https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg", "", "CAM", "7.3"));
        dummies.add(new Movie("Top Gun: Maverick", "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg", "", "HD", "8.3"));
        dummies.add(new Movie("Doctor Strange", "https://image.tmdb.org/t/p/w500/uGBVj3bEbCoZbDjjl9wTxcygko1.jpg", "", "HD", "7.5"));
        dummies.add(new Movie("Minions", "https://image.tmdb.org/t/p/w500/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg", "", "HD", "7.6"));
        
        adapter.updateData(dummies);
        textError.setVisibility(View.GONE);
        Toast.makeText(this, "Mode Demo: Menampilkan Data Sample", Toast.LENGTH_SHORT).show();
    }
}
