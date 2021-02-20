package com.example.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gifapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GifErrorFragment: Fragment() {
    private val activity = GifActivity()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gif_error, container, false)
        val tryBtn = view.findViewById(R.id.error_refresh_fab) as FloatingActionButton

        //TODO fix change fragment transaction
        tryBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,GifPageFragment())
                .commit()
        }
        return view
    }
}