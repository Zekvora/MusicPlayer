package com.zekvora.musicplayer;

import android.app.Application;
import android.widget.Toast;

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "App запущена", Toast.LENGTH_LONG).show();
        CrashHandler.install(this);
    }
}
