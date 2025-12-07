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
import java.util.ArrayList;
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

        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textError);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // Grid 3 Kolom
        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));

        // Setup Adapter
        adapter = new MovieAdapter(this, null, new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                // PERUBAHAN DI SINI: Buka Detail, bukan langsung Player
                bukaDetailFilm(movie);
            }
        });
        rvMovies.setAdapter(adapter);

        scraper = new AdicinemaxScraper();
        muatDataFilm();

        swipeRefresh.setOnRefreshListener(this::muatDataFilm);
    }

    private void muatDataFilm() {
        progressBar.setVisibility(View.VISIBLE);
        textError.setVisibility(View.GONE);

        scraper.getLatestMovies(new AdicinemaxScraper.ScrapeCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                    adapter.updateData(movies);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                    textError.setText(message);
                    textError.setVisibility(View.VISIBLE);
                    
                    // Kalau gagal, load data dummy buat pamer
                    isiDataDummy();
                });
            }
        });
    }

    private void bukaDetailFilm(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // Kita kirim seluruh data film (Judul, Poster, Rating, dll) ke halaman sebelah
        intent.putExtra("EXTRA_MOVIE", movie);
        startActivity(intent);
    }

    private void isiDataDummy() {
        ArrayList<Movie> dummies = new ArrayList<>();
        // Data Dummy Lengkap dengan URL Detail Palsu
        dummies.add(new Movie("Avatar: The Way of Water", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "https://adicinemax.com/avatar", "HD", "7.8"));
        dummies.add(new Movie("Black Adam", "https://image.tmdb.org/t/p/w500/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg", "https://adicinemax.com/adam", "HD", "6.9"));
        dummies.add(new Movie("Black Panther", "https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg", "https://adicinemax.com/panther", "CAM", "7.3"));
        dummies.add(new Movie("Top Gun: Maverick", "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg", "https://adicinemax.com/topgun", "HD", "8.3"));
        
        adapter.updateData(dummies);
        textError.setVisibility(View.GONE);
        Toast.makeText(this, "Mode Demo: Menampilkan Data Sample", Toast.LENGTH_SHORT).show();
    }
}
