package com.example.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple placeholder layout; reuse activity_main for now
        setContentView(R.layout.activity_main)
    }
}
