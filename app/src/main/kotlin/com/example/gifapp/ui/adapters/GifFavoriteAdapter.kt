package com.example.gifapp.ui.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gifapp.App
import com.example.gifapp.R
import com.example.gifapp.data.FileRepository
import com.example.gifapp.model.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "GifFavoriteAdapter"

class GifFavoriteAdapter(private var data: List<Gif>) :
    RecyclerView.Adapter<GifFavoriteViewHolder>() {
    private var repository: FileRepository? = null
    private var context = App.getInstance().applicationContext

    private var tracker: SelectionTracker<Long>? = null
    private var _selectedList: MutableList<String> = mutableListOf()

    private var _selectedCount = MutableStateFlow(_selectedList.size)
    val selectedCount: StateFlow<Int> = _selectedCount

    private var _selectedData = MutableStateFlow(data)
    val selectedData: StateFlow<List<Gif>> = _selectedData

    init {
        setHasStableIds(true)
        repository = FileRepository.getInstance(context)
    }

    /** Created by ID
     * date: 17-May-21, 10:24 AM
     * TODO: change to StateFlow
     */
    private var _gif = MutableLiveData<Gif>()
    val gif: LiveData<Gif> = _gif

    private var _isSelected = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean?> = _isSelected

    private var _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean?> = _isEditable

    fun updateData(list: List<Gif>) {
        this.data = list
        this.notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifFavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gif_favorite_item, parent, false)
        return GifFavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GifFavoriteViewHolder, position: Int) {
        val item = data[position]

        Glide.with(holder.itemView)
            .asGif()
            .load(Uri.parse(item.gifURL))
            .into(holder.imageView)

        holder.description.text = item.description
        holder.imageView.adjustViewBounds = true

        tracker?.let { it ->
            it.addObserver(
                object : SelectionTracker.SelectionObserver<Long>() {
                    override fun onSelectionChanged() {
                        val nItems: Int? = it.selection.size()
                        nItems?.let { items ->
                            if (items > 0) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    _isSelected.emit(true)
                                    _isEditable.emit(true)
                                }
                            } else {
                                CoroutineScope(Dispatchers.IO).launch {
                                    _isSelected.emit(false)
                                    _isEditable.emit(false)
                                }
                            }
                        }
                    }
                }
            )

            if (it.isSelected(position.toLong())) {
                Log.i(TAG, "--> onBindViewHolder: tracker.selected")
                holder.selectBtn.visibility = View.VISIBLE
                holder.itemView.scaleY = 0.9f
                holder.itemView.scaleX = 0.9f
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(TAG, "--> onBindViewHolder: selected.add[${item.id}]")
                    _selectedList.add(item.id)
                    Log.i(TAG, "--> onBindViewHolder: selected.list=[${_selectedList.size}]")
                    _selectedCount.emit(_selectedList.size)
                    Log.i(TAG, "--> onBindViewHolder: selected.count[${selectedCount.value}]")
                }
            } else {
                Log.i(TAG, "--> onBindViewHolder: tracker.unselected")
                holder.selectBtn.visibility = View.INVISIBLE
                holder.itemView.scaleY = 1.0f
                holder.itemView.scaleX = 1.0f
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(TAG, "--> onBindViewHolder: selected.drop[${item.id}]")
                    _selectedList.remove(item.id)
                    Log.i(TAG, "--> onBindViewHolder: selected.list=[${_selectedList.size}]")
                    _selectedCount.emit(_selectedList.size)
                    Log.i(TAG, "--> onBindViewHolder: selected.count[${selectedCount.value}]")
                }
            }
        }

        holder.itemView.setOnClickListener {
            _gif.postValue(item)
        }
    }

    override fun getItemCount() = data.size

    fun clearSelection() {
        CoroutineScope(Dispatchers.IO).launch {
            _isSelected.emit(false)
            _isEditable.emit(false)
            _selectedList.clear()
            _selectedCount.value = 0
        }
        tracker?.clearSelection()
    }

    fun deleteSelected() {
        _selectedList.let { list ->
            this.data = data.filterNot {
                list.contains(it.id)
            }
            Log.i(TAG, "--> deleteSelected: filtered.data[${list.size}]")
            list.forEach {
                Log.i(TAG, "--> deleteSelected: id[$it]")
                repository?.deleteById(it)
            }

            CoroutineScope(Dispatchers.IO).launch {
                _selectedData.emit(data)
            }
            list.clear()
        }

        notifyDataSetChanged()
    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }
}