package com.numan.videoeditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.numan.videoeditor.R
import com.numan.videoeditor.databinding.ItemsByFolderBinding
import com.numan.videoeditor.db.dataclass.ImageData
import com.numan.videoeditor.extensions.Extensions
import com.numan.videoeditor.extensions.Extensions.toast
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import java.util.*
import javax.inject.Inject

class ImageByAlbumAdapter @Inject constructor(
    private val glide: RequestManager,
    private val helpingMethodsUtils: HelpingMethodsUtils? = null
) :
    RecyclerView.Adapter<ImageByAlbumAdapter.MediumVerticalThumbsVH>() {


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


    var verticalSmallImagesThumbList: List<Any>
        get() = differ.currentList
        set(value) = differ.submitList(value)
*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediumVerticalThumbsVH {
        return MediumVerticalThumbsVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items_by_folder,
                parent,
                false
            )
        )


    }

    private fun getItem(pos: Int): ImageData? {
//        Extensions.logInfo("getItem: ${(helpingMethodsUtils?.getSelectedFolderId())}")
        return helpingMethodsUtils?.getImageByAlbum(helpingMethodsUtils.getSelectedFolderId())?.get(pos)
    }


    override fun getItemCount(): Int {
        return helpingMethodsUtils?.getImageByAlbum(helpingMethodsUtils.getSelectedFolderId())?.size!!
    }

    override fun onBindViewHolder(holder: MediumVerticalThumbsVH, position: Int) {
        when (holder) {
            is MediumVerticalThumbsVH -> {

                val data: ImageData? = getItem(position)
//                Extensions.logInfo("data: $data")
                glide.load(data?.imagePath).into(holder.binding.imageView1)
                holder.bind(data, position)

                holder.binding.clickableView.setOnClickListener {

                    if (helpingMethodsUtils?.getSelectedImages()!!.size < 30) {
                        if (holder.binding.imageView1.drawable == null) {
                            holder.itemView.context.toast("Image corrupted or not support.")
                            return@setOnClickListener
                        }
                        if (data?.imageCount == 0) {
                            helpingMethodsUtils.addSelectedImage(data)
                            notifyItemChanged(position)
                        } else {
                            holder.itemView.context.toast("Allready Selected")
                        }
                    } else {
                        holder.itemView.context.toast("Maximum 30 images can be select")
                    }

                } // end of onClick Listener
            }
        }
    }


    inner class MediumVerticalThumbsVH constructor(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        var binding = ItemsByFolderBinding.bind(itemView)

        fun bind(item: ImageData?, position: Int) {
            binding.cbSelect.isSelected = true
            var textView: TextView = binding.textView1
            val charSequence: CharSequence = if (item?.imageCount == 0) {
                ""
            } else {
                String.format("%02d", *arrayOf<Any>(Integer.valueOf(item?.imageCount!!)))
            }
            textView.text = charSequence

            textView = binding.textView1
            val i: Int = if (item.imageCount == 0) {
                0
            } else {
                1627373831
            }
            textView.setBackgroundColor(i)
            binding.clickableView.setOnClickListener {
                onItemClickListener?.invoke(position, item)
            }
        }


    }

    private var onItemClickListener: ((position: Int, ImageData) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, ImageData) -> Unit) {
        onItemClickListener = listener
    }
    /*
    interface Interaction {
        fun onItemSelected(position: Int, item: ImageData?)
    }*/


}

