package com.zekvora.musicplayer;

import android.app.Application;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Looper;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static void install(Application app) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
            new Handler(Looper.getMainLooper()).post(() -> {
                new AlertDialog.Builder(app)
                        .setTitle("КРАШ")
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", null)
                        .show();
            });
        });
    }
}
