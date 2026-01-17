package com.zekvora.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class PlaybackService extends Service {

    private ExoPlayer player;
    private PlayerNotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();

        // Уведомление для foreground
        notificationManager = new PlayerNotificationManager.Builder(this, 1, "playback_channel")
                .setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {
                    // Реализуй: title, description, icon
                })
                .setNotificationListener(new PlayerNotificationManager.NotificationListener() {
                    // Старт/стоп уведомления
                })
                .build();
        notificationManager.setPlayer(player);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Логика запуска трека из intent
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Или Binder для контроля из Activity
    }

    @Override
    public void onDestroy() {
        notificationManager.setPlayer(null);
        player.release();
        super.onDestroy();
    }
}