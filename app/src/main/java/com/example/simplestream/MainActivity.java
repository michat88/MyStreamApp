package com.example.simplestream;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Komponen Tampilan
    VideoView videoPlayer;
    ListView listFilmView;

    // Data Film (Judul dan Link)
    ArrayList<String> daftarJudul = new ArrayList<>();
    ArrayList<String> daftarLink = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Sambungkan kode dengan tampilan
        videoPlayer = findViewById(R.id.videoView);
        listFilmView = findViewById(R.id.movieList);

        // 2. Siapkan MediaController (Tombol Play/Pause/Geser)
        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(videoPlayer);

        // 3. PANGGIL PROVIDER (Data Tertanam Kita)
        muatDataFilm();

        // 4. Masukkan data ke List Tampilan
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_list_item_1, // Tampilan baris bawaan Android (teks putih/hitam standar)
            daftarJudul
        );
        listFilmView.setAdapter(adapter);

        // 5. Aksi kalau Judul diklik
        listFilmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ambil link sesuai urutan yang diklik
                String linkDipilih = daftarLink.get(position);
                String judulDipilih = daftarJudul.get(position);

                Toast.makeText(MainActivity.this, "Memutar: " + judulDipilih, Toast.LENGTH_SHORT).show();
                putarVideo(linkDipilih);
            }
        });
    }

    // --- FUNGSI PROVIDER TERTANAM ---
    private void muatDataFilm() {
        // Film 1: Big Buck Bunny (Tes)
        daftarJudul.add("Big Buck Bunny (Animasi)");
        daftarLink.add("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8");

        // Film 2: Sintel
        daftarJudul.add("Sintel (Animasi)");
        daftarLink.add("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");

        // Film 3: Tears of Steel
        daftarJudul.add("Tears of Steel (Sci-Fi)");
        daftarLink.add("https://content.jwplatform.com/manifests/yp34SRmf.m3u8");
        
        // KAMU BISA TAMBAH FILM LAIN DI BAWAH SINI
        // daftarJudul.add("Judul Film Kamu");
        // daftarLink.add("Link Video .mp4 atau .m3u8");
    }

    private void putarVideo(String url) {
        try {
            Uri videoUri = Uri.parse(url);
            videoPlayer.setVideoURI(videoUri);
            videoPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Gagal memutar video", Toast.LENGTH_SHORT).show();
        }
    }
}
