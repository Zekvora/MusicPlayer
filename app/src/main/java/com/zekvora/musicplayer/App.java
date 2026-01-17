package com.zekvora.musicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.Toast;

public class App extends Application {

    public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
    private static final String PLAYBACK_CHANNEL_NAME = "Музыкальное воспроизведение";
    private static final String PLAYBACK_CHANNEL_DESCRIPTION = "Уведомления для управления музыкой в фоне";

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "App запущена", Toast.LENGTH_LONG).show();
        createNotificationChannel();
        CrashHandler.install(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    PLAYBACK_CHANNEL_ID,
                    PLAYBACK_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription(PLAYBACK_CHANNEL_DESCRIPTION);
            channel.setShowBadge(false);
            channel.enableLights(false);
            channel.enableVibration(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
