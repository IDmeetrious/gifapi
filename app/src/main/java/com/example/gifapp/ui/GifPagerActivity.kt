package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gifapp.R
import com.example.gifapp.ui.random.GifRandomFragment
import com.google.android.material.tabs.TabLayout

private const val TAG = "GifActivity"

class GifPagerActivity : AppCompatActivity(R.layout.activity_gif_pager) {
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabLayout = findViewById(R.id.tab_layout)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GifRandomFragment())
                .commit()
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getString(R.string.latest) -> getLatest()
                    getString(R.string.top) -> getTop()
                    getString(R.string.random) -> {
                        getRandom()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabUnselected: ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabReselected: ${tab?.text}")
            }

        })
    }

    private fun getRandom() {
        Toast.makeText(this, "Random", Toast.LENGTH_SHORT).show()
    }

    private fun getTop() {
        Toast.makeText(this, "Top", Toast.LENGTH_SHORT).show()
    }

    private fun getLatest() {
        Toast.makeText(this, "Latest", Toast.LENGTH_SHORT).show()
    }

}