package com.example.simplestream;

import android.os.Handler;
import android.os.Looper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdicinemaxScraper {

    // URL Target (Bisa diganti jika domain berubah)
    private static final String BASE_URL = "https://adicinemax21.com"; // Ganti dengan URL aktif jika beda
    
    // Alat untuk bekerja di latar belakang (supaya aplikasi tidak macet)
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Interface untuk mengirim hasil data ke MainActivity
    public interface ScrapeCallback {
        void onSuccess(List<Movie> movies);
        void onError(String message);
    }

    // FUNGSI UTAMA: Mengambil Daftar Film Terbaru
    public void getLatestMovies(ScrapeCallback callback) {
        executor.execute(() -> {
            try {
                // 1. Koneksi ke Website
                Document doc = Jsoup.connect(BASE_URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(10000) // Batas waktu 10 detik
                        .get();

                List<Movie> movieList = new ArrayList<>();

                // 2. Cari elemen HTML pembungkus film
                // (Berdasarkan analisa umum layout web streaming: biasanya article atau div class='item')
                // Kita coba selector umum yang sering dipakai tema web film
                Elements movieElements = doc.select("article.item, div.movie-item, div.item");

                for (Element el : movieElements) {
                    // Ambil Judul
                    String title = el.select("h2.entry-title, .title, h3").text();
                    
                    // Ambil Link Poster
                    String posterUrl = el.select("img").attr("src");
                    if (posterUrl.isEmpty()) {
                         posterUrl = el.select("img").attr("data-src"); // Kadang web pakai lazy load
                    }

                    // Ambil Link Halaman Detail Film
                    String detailUrl = el.select("a").attr("href");

                    // Ambil Kualitas (HD/CAM)
                    String quality = el.select(".quality, .gmr-quality-item").text();

                    // Ambil Rating
                    String rating = el.select(".rating, .gmr-rating-item, .score").text();

                    // Validasi: Kalau judul ada, masukkan ke daftar
                    if (!title.isEmpty() && !detailUrl.isEmpty()) {
                        movieList.add(new Movie(title, posterUrl, detailUrl, quality, rating));
                    }
                }

                // 3. Kirim hasil ke tampilan utama
                handler.post(() -> {
                    if (!movieList.isEmpty()) {
                        callback.onSuccess(movieList);
                    } else {
                        callback.onError("Tidak ada film ditemukan. Cek struktur web.");
                    }
                });

            } catch (IOException e) {
                // Kalau internet mati atau web down
                handler.post(() -> callback.onError("Gagal mengambil data: " + e.getMessage()));
            }
        });
    }
}
