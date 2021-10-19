package com.numan.videoeditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.numan.videoeditor.R
import com.numan.videoeditor.databinding.GridSelectedItemBinding
import com.numan.videoeditor.db.dataclass.ImageData
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import java.util.*
import javax.inject.Inject

class SelectedImageAdapter @Inject constructor(
    private val glide: RequestManager,
    private val helpingMethodsUtils: HelpingMethodsUtils? = null
) :
    RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder>() {
    val TYPE_BLANK = 1
    val TYPE_IMAGE = 0
    var isExpanded = false

/*

    val diffCallback = object : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return false
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var selectedImageList: List<Any>
        get() = differ.currentList
        set(value) = differ.submitList(value)

*/

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (!isExpanded && position >= helpingMethodsUtils?.getSelectedImages()?.size!!) {
            TYPE_BLANK
        } else TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.grid_selected_item,
                parent,
                false
            )
        )


    }

    private fun getItem(pos: Int): ImageData {
        val list: ArrayList<ImageData> = helpingMethodsUtils?.getSelectedImages()!!
        return if (list.size <= pos) {
            ImageData()
        } else list[pos]
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        when (holder) {
            is SelectedImageViewHolder -> {
                if (getItemViewType(position) == TYPE_BLANK) {
                    holder.binding.root.visibility = View.INVISIBLE
                    return
                }
                holder.binding.root.visibility = View.VISIBLE
                val data: ImageData = getItem(position)
                glide.load(data.imagePath).into(holder.binding.ivThumb)
                holder.bind(data)
//                notifyItemInserted(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        val list: ArrayList<ImageData>? = helpingMethodsUtils?.getSelectedImages()
        return if (isExpanded) {
            list?.size!!
        } else list?.size?.plus(20)!!
    }


    inner class SelectedImageViewHolder constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        var binding = GridSelectedItemBinding.bind(itemView)

        fun bind(item: ImageData?) {
            item?.let {
                binding.ivRemove.setOnClickListener {
                    onItemRemoved?.invoke(adapterPosition, item)
                }
            }

        }


    }

    private var onItemRemoved: ((position: Int, ImageData) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, ImageData) -> Unit) {
        onItemRemoved = listener
    }
//    interface Interaction {
//        fun onItemRemoved(position: Int, item: ImageData?)
//    }


}