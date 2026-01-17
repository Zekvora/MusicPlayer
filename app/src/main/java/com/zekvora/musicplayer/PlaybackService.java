package com.zekvora.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class PlaybackService extends Service {

    private ExoPlayer player;
    private PlayerNotificationManager notificationManager;

    private static final int NOTIFICATION_ID = 1001; // любой уникальный > 0

    @Override
    public void onCreate() {
        super.onCreate();

        player = new ExoPlayer.Builder(this).build();

        // Создаём уведомление-строитель (простое, чтобы быстро показать)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.PLAYBACK_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("MusicPlayer")
                .setContentText("Готов к воспроизведению")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true); // не смахивается

        // Запускаем как foreground СРАЗУ (важно!)
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);

        // Теперь можно создать PlayerNotificationManager (он заменит простое уведомление на красивое)
        notificationManager = new PlayerNotificationManager.Builder(
                this,
                NOTIFICATION_ID,
                App.PLAYBACK_CHANNEL_ID
        )
                .setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public CharSequence getCurrentContentTitle(Player player) {
                        return player.getCurrentMediaItem() != null
                                ? player.getCurrentMediaItem().mediaMetadata.title
                                : "Музыка играет";
                    }

                    @Override
                    public CharSequence getCurrentContentText(Player player) {
                        return player.getCurrentMediaItem() != null
                                ? player.getCurrentMediaItem().mediaMetadata.artist
                                : "Неизвестный исполнитель";
                    }

                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(PlaybackService.this, MainActivity.class);
                        return PendingIntent.getActivity(
                                PlaybackService.this,
                                0,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        );
                    }

                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return null; // позже добавь обложку
                    }
                })
                .setSmallIconResourceId(android.R.drawable.ic_media_play)
                .build();

        notificationManager.setPlayer(player);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Здесь можно добавить логику запуска трека из intent
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (notificationManager != null) {
            notificationManager.setPlayer(null);
        }
        if (player != null) {
            player.release();
            player = null;
        }
        super.onDestroy();
    }
}
