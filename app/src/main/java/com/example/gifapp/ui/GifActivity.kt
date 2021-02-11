package com.example.gifapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gifapp.R

private const val TAG = "GifActivity"

class GifActivity : AppCompatActivity(R.layout.activity_gif) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GifFragment())
                .commit()
        }
    }
}