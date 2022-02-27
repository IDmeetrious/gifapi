package com.example.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.ui.adapters.GifFavoriteAdapter
import com.example.gifapp.ui.adapters.GifFavoriteLookup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GifFavoriteFragment : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: GifFavoriteAdapter
    private lateinit var repository: FileRepository
    private var selectedCount: Int = 0
    private var fullScreenFragment: GifFullScreenFragment? = null
    private var fmCallbackInstance: FragmentManager.FragmentLifecycleCallbacks? = null

    private var tracker: SelectionTracker<Long>? = null

    private lateinit var bottomLayout: ConstraintLayout
    private lateinit var deleteBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var selectedTv: TextView
    private lateinit var selectAllCb: CheckBox
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    private var sheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)
        if (fullScreenFragment == null) fullScreenFragment = GifFullScreenFragment()

        context?.let { repository = FileRepository.getInstance(it) }
    }

    override fun onStart() {
        super.onStart()

        addDialogStatusListener()
        addBottomSheetCallbacks()

        adapter.updateData(repository.loadFromDatabase())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        adapter = GifFavoriteAdapter(emptyList())
        adapter.gif.observe(viewLifecycleOwner) { gif ->
            gif?.let {
                val args = Bundle().apply {
                    putString(GIF_ID, it.id)
                    putString(GIF_DESC, it.description)
                    putString(GIF_URI, it.gifURL)
                }
                if (adapter.isEditable.value == false) {
                    fullScreenFragment?.arguments = args
                    fullScreenFragment?.show(childFragmentManager, "GifFullScreenFragment")
                }
            }
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)

        lifecycleScope.launch {
            adapter.isSelected.collect {
                it?.let {
                    if (it) sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            adapter.selectedData.collect {
                adapter.updateData(it)
            }
        }

        selectAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (i in 0..adapter.itemCount) {
                    tracker?.select(i.toLong())
                }
            } else {
                for (i in 0..adapter.itemCount) {
                    tracker?.deselect(i.toLong())
                }
            }
        }

        lifecycleScope.launch {
            adapter.selectedCount.collect {
                selectedCount = it
                selectedTv.text = "Selected: $it"
                if (it == 0) selectAllCb.isChecked = false
            }
        }
    }

    private fun addDialogStatusListener() {
        fmCallbackInstance = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                super.onFragmentStopped(fm, f)
                if (f == fullScreenFragment) adapter.updateData(repository.getFavoriteList())
            }
        }
        fmCallbackInstance?.let {
            childFragmentManager.registerFragmentLifecycleCallbacks(it, true)
        }
    }

    private fun initViews(view: View) {
        rv = view.findViewById(R.id.favorite_rv)
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.setHasFixedSize(true)
        rv.adapter = adapter

        // Multi selection
        tracker = SelectionTracker.Builder(
            "selection_to_delete", rv, StableIdKeyProvider(rv),
            GifFavoriteLookup(rv), StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        adapter.setTracker(tracker)

        bottomLayout = view.findViewById(R.id.favorite_top_menu)
        deleteBtn = view.findViewById(R.id.favorite_bottom_delete_btn)
        cancelBtn = view.findViewById(R.id.favorite_bottom_cancel_btn)
        selectAllCb = view.findViewById(R.id.favorite_bottom_check)
        selectedTv = view.findViewById(R.id.favorite_bottom_select_tv)
        selectedTv.text = "Selected: $selectedCount"

        deleteBy()

        sheetBehavior = BottomSheetBehavior.from(bottomLayout)
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun addBottomSheetCallbacks() {
        sheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> adapter.clearSelection()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        }
        sheetCallback?.let {
            sheetBehavior?.addBottomSheetCallback(it)
        }
    }

    private fun deleteBy() {
        lifecycleScope.launch {
            adapter.isSelected.collect {
                it?.let {
                    if (it) {
                        cancelBtn.setOnClickListener {
                            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                        deleteBtn.setOnClickListener {
                            adapter.deleteSelected()
                            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    } else {
                        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
        }
    }

    private fun updateFavoriteList() {
        val list = repository.loadFromStorage()
        adapter.updateData(list)
    }

    override fun onPause() {
        super.onPause()
        adapter.clearSelection()
    }

    override fun onResume() {
        super.onResume()
        adapter.updateData(repository.loadFromDatabase())
    }

    override fun onStop() {
        super.onStop()

        fmCallbackInstance?.let {
            childFragmentManager.unregisterFragmentLifecycleCallbacks(it)
        }

        sheetCallback?.let {
            sheetBehavior?.removeBottomSheetCallback(it)
        }
    }

    override fun onDestroy() {
        fullScreenFragment = null
        super.onDestroy()
    }

    companion object {
        const val GIF_ID = "gif_id"
        const val GIF_DESC = "gif_desc"
        const val GIF_URI = "gif_uri"
    }
}