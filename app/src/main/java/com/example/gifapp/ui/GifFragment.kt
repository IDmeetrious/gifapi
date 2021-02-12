package com.example.gifapp.ui

import androidx.fragment.app.Fragment
import com.example.gifapp.R

private const val TAG = "GifFragment"

class GifFragment : Fragment(R.layout.fragment_gif_random) {
/*
    private val viewModel: GifViewModel by lazy {
        ViewModelProvider(this).get(GifViewModel::class.java)
    }
    private lateinit var gifIv: ImageView
    private lateinit var descTv: TextView
    private lateinit var nextFab: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            Log.d(TAG, "--> arguments.key: ${arguments?.containsKey(ARG_OBJECT)}")
        }

        gifIv = view.findViewById(R.id.gif_iv)
        descTv = view.findViewById(R.id.gif_desc_tv)
//        nextFab = view.findViewById(R.id.gif_next_fab)

        viewModel.randomGif.observe(viewLifecycleOwner, {
            try {
                Glide.with(view.context)
                    .load(it.gifURL)
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gifIv)
                descTv.text = it.description
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        })

        nextFab.setOnClickListener {
            viewModel.getRandom()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(requireContext()).clearDiskCache()
    }*/
}