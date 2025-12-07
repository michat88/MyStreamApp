package com.example.simplestream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgBackdrop, imgPoster;
    private TextView textTitle, textMeta, textSynopsis;
    private MaterialButton btnPlay;
    private ImageButton btnBack;
    private Movie movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 1. Hubungkan variabel dengan Tampilan XML
        imgBackdrop = findViewById(R.id.imgBackdrop);
        imgPoster = findViewById(R.id.imgPoster);
        textTitle = findViewById(R.id.textTitle);
        textMeta = findViewById(R.id.textMeta);
        textSynopsis = findViewById(R.id.textSynopsis);
        btnPlay = findViewById(R.id.btnPlay);
        btnBack = findViewById(R.id.btnBack);

        // 2. Ambil Data Film yang dikirim dari Halaman Utama
        movieData = (Movie) getIntent().getSerializableExtra("EXTRA_MOVIE");

        // 3. Tampilkan Data ke Layar
        if (movieData != null) {
            setupTampilan();
        } else {
            Toast.makeText(this, "Data film error!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 4. Aksi Tombol Play
        btnPlay.setOnClickListener(v -> putarFilm());

        // 5. Aksi Tombol Kembali
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupTampilan() {
        // Set Judul
        textTitle.setText(movieData.getTitle());

        // Set Info Tambahan (Rating & Kualitas)
        String info = movieData.getQuality() + " • Rating: " + movieData.getRating();
        textMeta.setText(info);

        // Set Sinopsis (Karena scraper kita belum ambil sinopsis lengkap, kita buat default dulu)
        // Nanti bisa dikembangkan untuk ambil sinopsis dari 'detailUrl'
        textSynopsis.setText("Saksikan keseruan film " + movieData.getTitle() + " dengan kualitas terbaik hanya di Adicinemax Player.");

        // Muat Poster Kecil
        Glide.with(this)
                .load(movieData.getPosterUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imgPoster);

        // Muat Gambar Latar (Backdrop)
        // Karena Adicinemax cuma kasih 1 gambar, kita pakai gambar yang sama tapi kita zoom/crop
        Glide.with(this)
                .load(movieData.getPosterUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imgBackdrop);
    }

    private void putarFilm() {
        Intent intent = new Intent(DetailActivity.this, PlayerActivity.class);
        
        // PENTING: Simulasi Link Video
        // Karena scraper Adicinemax butuh bypass rumit untuk dapat link asli,
        // kita pakai link HLS premium (Tears of Steel) untuk demonstrasi player canggih.
        // Jika kamu nanti punya kode extractor asli, ganti link ini dengan hasil extract.
        String videoLink = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8";
        
        intent.putExtra("VIDEO_URL", videoLink);
        startActivity(intent);
    }
}
