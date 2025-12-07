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

        // 1. Inisialisasi Komponen Tampilan
        rvMovies = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textError);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // 2. Setup Grid (3 Kolom agar mirip aplikasi profesional)
        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));

        // 3. Setup Adapter (Penghubung Data ke Tampilan)
        adapter = new MovieAdapter(this, null, new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                // Aksi saat poster diklik
                bukaDetailFilm(movie);
            }
        });
        rvMovies.setAdapter(adapter);

        // 4. Siapkan Mesin Pencari Data (Scraper)
        scraper = new AdicinemaxScraper();

        // 5. Mulai Ambil Data Film
        muatDataFilm();

        // 6. Fitur Tarik untuk Refresh
        swipeRefresh.setOnRefreshListener(this::muatDataFilm);
    }

    private void muatDataFilm() {
        progressBar.setVisibility(View.VISIBLE);
        textError.setVisibility(View.GONE);

        scraper.getLatestMovies(new AdicinemaxScraper.ScrapeCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                // Wajib dijalankan di Thread Utama (UI Thread)
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
                    
                    // Jika gagal ambil data asli, tampilkan data dummy (cadangan)
                    isiDataDummy();
                });
            }
        });
    }

    // --- FUNGSI NAVIGASI DENGAN PENGAMAN ANTI-CRASH ---
    private void bukaDetailFilm(Movie movie) {
        try {
            // Cek 1: Pastikan data film tidak kosong
            if (movie == null) {
                Toast.makeText(this, "Data film error (Kosong)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek 2: Coba buka halaman Detail
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("EXTRA_MOVIE", movie); // Kirim data film
            startActivity(intent);

        } catch (Exception e) {
            // PENGAMAN: Kalau ada error (misal lupa daftar activity), tangkap di sini
            e.printStackTrace();
            // Tampilkan pesan error di layar supaya kita tahu salahnya di mana
            Toast.makeText(this, "Gagal membuka detail: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void isiDataDummy() {
        ArrayList<Movie> dummies = new ArrayList<>();
        // Data Dummy untuk Demo
        dummies.add(new Movie("Avatar: The Way of Water", "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg", "https://adicinemax.com/avatar", "HD", "7.8"));
        dummies.add(new Movie("Black Adam", "https://image.tmdb.org/t/p/w500/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg", "https://adicinemax.com/adam", "HD", "6.9"));
        dummies.add(new Movie("Black Panther", "https://image.tmdb.org/t/p/w500/sv1xJUazXeYqALzczSZ3O6nkH75.jpg", "https://adicinemax.com/panther", "CAM", "7.3"));
        dummies.add(new Movie("Top Gun: Maverick", "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg", "https://adicinemax.com/topgun", "HD", "8.3"));
        dummies.add(new Movie("Doctor Strange", "https://image.tmdb.org/t/p/w500/uGBVj3bEbCoZbDjjl9wTxcygko1.jpg", "https://adicinemax.com/strange", "HD", "7.5"));
        dummies.add(new Movie("Minions", "https://image.tmdb.org/t/p/w500/wKiOkZTN9lUUUNZLmtnwubZYONg.jpg", "https://adicinemax.com/minions", "HD", "7.6"));
        
        adapter.updateData(dummies);
        textError.setVisibility(View.GONE);
        Toast.makeText(this, "Mode Demo: Menampilkan Data Sample", Toast.LENGTH_SHORT).show();
    }
}
