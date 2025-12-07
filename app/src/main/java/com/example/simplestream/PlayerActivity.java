package com.example.simplestream;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

public class PlayerActivity extends AppCompatActivity {

    private StyledPlayerView playerView;
    private ExoPlayer player;
    private ProgressBar loadingBar;
    private String videoUrl;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.player_view);
        loadingBar = findViewById(R.id.loading);

        // Ambil Link Video yang dikirim dari Halaman Utama
        videoUrl = getIntent().getStringExtra("VIDEO_URL");

        if (videoUrl == null || videoUrl.isEmpty()) {
            Toast.makeText(this, "Link video tidak ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Buat Mesin ExoPlayer
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);

            // 2. Siapkan Sumber Data (Internet)
            // Kita palsukan User-Agent biar server mengira kita buka dari Browser Chrome
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .setAllowCrossProtocolRedirects(true);

            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource;

            // Cek apakah ini file streaming (.m3u8) atau video biasa (.mp4)
            if (videoUrl.contains(".m3u8")) {
                mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            } else {
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri));
            }

            // 3. Mulai Putar
            player.setMediaSource(mediaSource);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();

            // 4. Dengarkan Status Player (Loading/Error)
            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    if (state == Player.STATE_BUFFERING) {
                        loadingBar.setVisibility(View.VISIBLE); // Munculkan loading
                    } else {
                        loadingBar.setVisibility(View.GONE); // Sembunyikan loading
                    }
                }

                @Override
                public void onPlayerError(PlaybackException error) {
                    Toast.makeText(PlayerActivity.this, "Gagal memutar: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    loadingBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentMediaItemIndex();
            player.release();
            player = null;
        }
    }

    // --- LOGIKA LAYAR PENUH (IMMERSIVE) ---
    private void hideSystemUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // Untuk HP Android lama
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    // --- SIKLUS HIDUP APLIKASI (Supaya memori aman) ---

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) releasePlayer();
    }
}
