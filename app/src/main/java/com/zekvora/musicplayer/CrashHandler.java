package com.zekvora.musicplayer;

import android.app.Application;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final Application app;

    public CrashHandler(Application app) {
        this.app = app;
    }

    public static void install(Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(app));
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String msg = e.getClass().getSimpleName() + ": " + e.getMessage();

        new Handler(Looper.getMainLooper()).post(() -> {
            new AlertDialog.Builder(app)
                    .setTitle("КРАШ")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("ВЫЙТИ", (d, w) -> {
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                    })
                    .show();
        });

        // Через 5 секунд убиваем процесс, если пользователь не нажал
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }, 5000);
    }
}
