package com.example.simplestream;

import android.app.Activity; // Kita pakai Activity biasa, bukan AppCompat
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Toast;
import java.util.ArrayList;

// Perhatikan: extends Activity (Bukan AppCompatActivity)
public class MainActivity extends Activity {

    VideoView videoPlayer;
    ListView listFilmView;
    ArrayList<String> daftarJudul = new ArrayList<>();
    ArrayList<String> daftarLink = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPlayer = findViewById(R.id.videoView);
        listFilmView = findViewById(R.id.movieList);

        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(videoPlayer);

        muatDataFilm();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, 
            android.R.layout.simple_list_item_1, 
            daftarJudul
        );
        listFilmView.setAdapter(adapter);

        listFilmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String linkDipilih = daftarLink.get(position);
                String judulDipilih = daftarJudul.get(position);
                Toast.makeText(MainActivity.this, "Memutar: " + judulDipilih, Toast.LENGTH_SHORT).show();
                putarVideo(linkDipilih);
            }
        });
    }

    private void muatDataFilm() {
        daftarJudul.add("Big Buck Bunny");
        daftarLink.add("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8");

        daftarJudul.add("Sintel");
        daftarLink.add("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");

        daftarJudul.add("Tears of Steel");
        daftarLink.add("https://content.jwplatform.com/manifests/yp34SRmf.m3u8");
    }

    private void putarVideo(String url) {
        try {
            Uri videoUri = Uri.parse(url);
            videoPlayer.setVideoURI(videoUri);
            videoPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
