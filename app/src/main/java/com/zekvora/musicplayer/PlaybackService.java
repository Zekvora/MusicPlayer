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

    // Уникальный ID уведомления (должен быть постоянным для одного уведомления)
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        // Инициализация ExoPlayer
        player = new ExoPlayer.Builder(this).build();

        // Создаём менеджер уведомлений
        notificationManager = new PlayerNotificationManager.Builder(
                this,
                NOTIFICATION_ID,
                App.PLAYBACK_CHANNEL_ID
        )
                .setMediaDescriptionAdapter(new PlayerNotificationManager.MediaDescriptionAdapter() {

                    @Override
                    public CharSequence getCurrentContentTitle(Player player) {
                        // Название трека — пока заглушка, потом берёшь из metadata
                        return player.getCurrentMediaItem() != null
                                ? player.getCurrentMediaItem().mediaMetadata.title
                                : "Музыка играет";
                    }

                    @Override
                    public CharSequence getCurrentContentText(Player player) {
                        // Подтекст — артист или альбом
                        return player.getCurrentMediaItem() != null
                                ? player.getCurrentMediaItem().mediaMetadata.artist
                                : "Неизвестный исполнитель";
                    }

                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        // Клик по уведомлению → открывает MainActivity
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
                        // Пока возвращаем null (уведомление без большой иконки)
                        // Позже можно добавить загрузку через Glide и callback.onBitmapLoaded()
                        return null;
                    }
                })
                .setSmallIconResourceId(android.R.drawable.ic_media_play)  // маленькая иконка в статус-баре
                .setChannelNameResourceId(android.R.string.ok) // можно заменить на свою строку
                .build();

        // Привязываем плеер к менеджеру уведомлений
        notificationManager.setPlayer(player);

        // Запускаем сервис как foreground (обязательно для Android 9+)
        // Уведомление будет создано автоматически PlayerNotificationManager
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Здесь можно обрабатывать команды: play, pause, новая песня и т.д.
        // Пока просто возвращаем START_STICKY — сервис перезапустится при убийстве
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Пока не используем binding
    }

    @Override
    public void onDestroy() {
        // Отключаем уведомление и освобождаем ресурсы
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
