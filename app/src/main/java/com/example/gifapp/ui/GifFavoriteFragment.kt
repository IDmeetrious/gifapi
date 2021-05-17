package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import com.example.gifapp.ui.adapters.GifFavoriteAdapter
import com.example.gifapp.utils.Constants.GIF_DESC
import com.example.gifapp.utils.Constants.GIF_ID
import com.example.gifapp.utils.Constants.GIF_URI
import com.google.android.material.bottomsheet.BottomSheetBehavior

private const val TAG = "GifFavoriteFragment"

class GifFavoriteFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: GifFavoriteAdapter
    private lateinit var repository: FileRepository
    private var gif = Gif()

    private var isEditable: Boolean = false

    private lateinit var bottomLayout: ConstraintLayout
    private lateinit var deleteBtn: Button
    private lateinit var cancelBtn: Button
    private var sheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        adapter = GifFavoriteAdapter(listOf())
        adapter.gif.observe(viewLifecycleOwner, { mGif ->
            gif = mGif
            val args = Bundle().apply {
                Log.i(TAG, "--> onCreateView: id[${gif.id}]")
                this.putString(GIF_ID, gif.id)
                Log.i(TAG, "--> onCreateView: description[${gif.description}]")
                this.putString(GIF_DESC, gif.description)
                Log.i(TAG, "--> onCreateView: uri[${gif.gifURL}]")
                this.putString(GIF_URI, gif.gifURL)
            }


//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, GifFullScreenFragment::class.java, arguments, TAG)
//                .commit()
            if (adapter.isSelected.value != true) {
                // If dialog was created dismiss
                Log.i(TAG, "--> onCreateView: moveToFullScreen")

                val fragment = GifFullScreenFragment()
                fragment.arguments = args
                fragment.show(childFragmentManager, "GifFullScreenFragment")

            } else {
                isEditable = true
//                dialog.show(childFragmentManager, "GifFavoriteBottomDialog")
                sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })

        repository = FileRepository.getInstance(requireContext())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "--> onViewCreated: ")
        initViews(view)
    }

    private fun initViews(view: View) {
        rv = view.findViewById(R.id.favorite_rv)
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.adapter = adapter

        bottomLayout = view.findViewById(R.id.favorite_top_menu)
        deleteBtn = view.findViewById(R.id.favorite_bottom_delete_btn)
        cancelBtn = view.findViewById(R.id.favorite_bottom_cancel_btn)

        deleteBy()

        sheetBehavior = BottomSheetBehavior.from(bottomLayout)
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.i(
                            TAG,
                            "--> onStateChanged: State_Hidden"
                        )
                        adapter.clearSelection()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> Log.i(
                        TAG,
                        "--> onStateChanged: State_Expanded"
                    )
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.i(
                        TAG,
                        "--> onStateChanged: State_Collapsed"
                    )
                    BottomSheetBehavior.STATE_DRAGGING -> Log.i(
                        TAG,
                        "--> onStateChanged: State_Dragging"
                    )
                    BottomSheetBehavior.STATE_SETTLING -> Log.i(
                        TAG,
                        "--> onStateChanged: State_Setting"
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.i(TAG, "--> onSlide: ")
            }

        })

    }

    private fun deleteBy() {
        adapter.isSelected.observe(viewLifecycleOwner, {
            if (it) {
                /** Created by ID
                 * date: 17-May-21, 4:08 PM
                 * TODO: delete items by id
                 */
                cancelBtn.setOnClickListener {
                    Log.i(TAG, "--> deleteBy: Clicked Cancel")
                    sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
                deleteBtn.setOnClickListener {
                    Log.i(TAG, "--> deleteBy: Clicked Delete")
                }
            } else { sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN }
        })
    }

    private fun updateFavoriteList() {
        val list = repository.loadFromStorage()
        Log.i(TAG, "--> updateFavoriteList: ${list.size}")
        adapter.updateData(list)
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "--> onPause: ")
        adapter.clearSelection()
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "--> onResume: ")

        updateFavoriteList()
    }

    override fun onStop() {
        super.onStop()
        arguments = null
    }
}