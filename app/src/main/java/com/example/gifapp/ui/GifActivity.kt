package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gifapp.R

private const val TAG = "GifActivity"

class GifActivity : AppCompatActivity(R.layout.activity_gif), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GifCategoriesFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    fun onRefreshActivity(){
        Log.i(TAG, "--> onRefreshActivity: ")
        this.onRestart()
    }
    override fun onRefresh() {
        Log.i(TAG, "--> onSwipeLayoutRefresh: ")
    }

}