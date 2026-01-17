package com.zekvora.musicplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private TextView trackTitle, trackArtist;
    private Slider seekBar;
    private MaterialButton btnPlayPause;
    private ImageView albumArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация views
        albumArt = findViewById(R.id.album_art);
        trackTitle = findViewById(R.id.track_title);
        trackArtist = findViewById(R.id.track_artist);
        seekBar = findViewById(R.id.seek_bar);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        MaterialButton btnPrevious = findViewById(R.id.btn_previous);
        MaterialButton btnNext = findViewById(R.id.btn_next);

        // Инициализация ExoPlayer (лучше, чем MediaPlayer, уже в deps)
        player = new ExoPlayer.Builder(this).build();

        // Пример: загрузка тестового трека (замени на реальный URI из хранилища)
        MediaItem mediaItem = MediaItem.fromUri("https://example.com/test.mp3");  // Или local file URI
        player.setMediaItem(mediaItem);
        player.prepare();

        // Кнопки
        btnPlayPause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                btnPlayPause.setIconResource(android.R.drawable.ic_media_play);
            } else {
                player.play();
                btnPlayPause.setIconResource(android.R.drawable.ic_media_pause);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            // Логика предыдущего трека (добавь позже)
        });

        btnNext.setOnClickListener(v -> {
            // Логика следующего трека
        });

        // Обновление seekbar (в отдельном потоке)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    seekBar.setValue(player.getCurrentPosition());
                    seekBar.postDelayed(this, 1000);
                }
            }
        });

        seekBar.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) player.seekTo((long) value);
        });

        seekBar.setValueTo(player.getDuration());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}