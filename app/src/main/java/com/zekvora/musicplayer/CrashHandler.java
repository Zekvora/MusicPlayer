package com.zekvora.musicplayer;

import android.app.Application;
import android.content.DialogInterface;
import android.os.Process;
import androidx.appcompat.app.AlertDialog;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final Application app;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public CrashHandler(Application app) {
        this.app = app;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static void install(Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(app));
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        String msg = getStackTraceString(throwable);

        // Показываем диалог в UI-потоке
        new android.os.Handler(app.getMainLooper()).post(() -> {
            new AlertDialog.Builder(app)
                    .setTitle("ОШИБКА")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("ВЫЙТИ", (d, w) -> {
                        d.dismiss();
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    })
                    .show();
        });

        // Через 5 секунды убиваем процесс (чтобы диалог не висел вечно)
        new android.os.Handler(app.getMainLooper()).postDelayed(() -> {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }, 5000);

        // Запускаем старый обработчик на всякий случай
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        }
    }

    private static String getStackTraceString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage()).append("\n\n");
        for (StackTraceElement el : e.getStackTrace()) {
            sb.append("  at ").append(el).append('\n');
        }
        return sb.toString();
    }
}
