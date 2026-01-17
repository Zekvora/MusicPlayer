package com.zekvora.musicplayer;

import android.app.Application;
import android.widget.Toast;

package com.zekvora.musicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.Toast;

public class App extends Application {

    // Константа для ID канала (должна совпадать с тем, что используешь в PlayerNotificationManager)
    public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
    private static final String PLAYBACK_CHANNEL_NAME = "Музыкальное воспроизведение";
    private static final String PLAYBACK_CHANNEL_DESCRIPTION = "Уведомления для управления музыкой в фоне";

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "App запущена", Toast.LENGTH_LONG).show();

        // Создаём канал уведомлений (только для Android 8.0+)
        createNotificationChannel();

        // Устанавливаем глобальный обработчик крашей
        CrashHandler.install(this);
    }

    private void createNotificationChannel() {
        // Проверяем версию Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    PLAYBACK_CHANNEL_ID,
                    PLAYBACK_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW  // LOW — не беспокоит пользователя звуком/вибрацией
            );

            channel.setDescription(PLAYBACK_CHANNEL_DESCRIPTION);
            channel.setShowBadge(false);              // Не показывать бейдж на иконке приложения
            channel.enableLights(false);              // Без мигания
            channel.enableVibration(false);           // Без вибрации

            // Регистрируем канал в системе
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}