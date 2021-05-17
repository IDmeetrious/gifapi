package com.example.gifapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.utils.Constants.GIF_ID
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "GifFavoriteBottomDialog"

class GifFavoriteBottomDialog : BottomSheetDialogFragment() {

    private lateinit var checkBox: CheckBox
    private lateinit var selectedTv: TextView
    private lateinit var deleteBtn: Button
    private lateinit var cancelBtn: Button
    private var gifId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "--> onCreate: ")
        initDialog()
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_bottom_dialog, container, false)

        Log.i(TAG, "--> onCreateView: ")
        return rootView
    }

    private fun initDialog() {
        // Customize dialog
        this.isCancelable = false
        this.setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        // Receive bundle
        arguments?.let {
            gifId = it.getString(GIF_ID, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "--> onViewCreated: ")
        initViews(view)
    }

    private fun initViews(view: View) {
        view.let {
            checkBox = it.findViewById(R.id.favorite_bottom_check)
            selectedTv = it.findViewById(R.id.favorite_bottom_select_tv)
            deleteBtn = it.findViewById(R.id.favorite_bottom_delete_btn)
            cancelBtn = it.findViewById(R.id.favorite_bottom_cancel_btn)

//            val bottomLayout = it.findViewById(R.id.favorite_top_menu) as ConstraintLayout
//            val sheetBehavior = BottomSheetBehavior.from(bottomLayout)
//            sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    when(newState){
//                        BottomSheetBehavior.STATE_HIDDEN -> Log.i(
//                            TAG,
//                            "--> onStateChanged: State_Hidden"
//                        )
//                        BottomSheetBehavior.STATE_EXPANDED -> Log.i(
//                            TAG,
//                            "--> onStateChanged: State_Expanded"
//                        )
//                        BottomSheetBehavior.STATE_COLLAPSED -> Log.i(TAG, "--> onStateChanged: State_Collapsed")
//                        BottomSheetBehavior.STATE_DRAGGING -> Log.i(
//                            TAG,
//                            "--> onStateChanged: State_Dragging"
//                        )
//                        BottomSheetBehavior.STATE_SETTLING -> Log.i(
//                            TAG,
//                            "--> onStateChanged: State_Setting"
//                        )
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    Log.i(TAG, "--> onSlide: ")
//                }
//
//            })
        }

        cancelBtn.setOnClickListener {
            Log.i(TAG, "--> initViews: onCancel")
            dismiss()
        }

        deleteBtn.setOnClickListener {
            Log.i(TAG, "--> initViews: onDelete")
            CoroutineScope(Dispatchers.IO).launch {
                FileRepository.getInstance(requireContext())
                    .deleteById(gifId)
            }
        }

    }
}