package com.numan.videoeditor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.numan.videoeditor.R
import com.numan.videoeditor.databinding.ItemsBinding
import com.numan.videoeditor.db.dataclass.ImageData
import com.numan.videoeditor.extensions.Extensions
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import java.util.*
import javax.inject.Inject

/*
* TODO 1: AlbumAdapterById:: When clicking on items here it's getting wrong
*  folder names which causes not to show images in ImageByAlbumAdapter
*
* */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlbumAdapterById @Inject constructor(
    private val glide: RequestManager,
    private val helpingMethodsUtils: HelpingMethodsUtils? = null
) :
    RecyclerView.Adapter<AlbumAdapterById.SmallVerticalThumbsVH>() {

    val folderId_: MutableSet<String?>? = helpingMethodsUtils?.getAllAlbum()?.keys
    var folderId: MutableList<String?>? = folderId_?.toMutableList()

//    val diffCallback = object : DiffUtil.ItemCallback<Any>() {
//
//        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//            return false
//        }
//
//        @SuppressLint("DiffUtilEquals")
//        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//    private val differ = AsyncListDiffer(this, diffCallback)
//
//
//    var verticalSmallImagesThumbList: List<Any>
//        get() = differ.currentList
//        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallVerticalThumbsVH {
        return SmallVerticalThumbsVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items,
                parent,
                false
            )
        )


    }

    internal class C06281 : Comparator<String?> {
        override fun compare(s1: String?, s2: String?): Int {
            return s1?.compareTo(s2!!, ignoreCase = true) ?: 0
        }
    }

    fun AlbumAdapterById(activity: Context?) {

        Collections.sort(folderId, C06281())
        if (folderId!!.size != 0) {
            helpingMethodsUtils?.setSelectedFolderId(folderId?.get(0))
        }

    }

    private fun getItem(pos: Int): String? {
        return folderId?.get(pos)
    }

    override fun onBindViewHolder(holder: SmallVerticalThumbsVH, position: Int) {
        when (holder) {
            is SmallVerticalThumbsVH -> {
//                Extensions.logInfo("folderId Adapter: $folderId")

                /*
                * Vertical Row Single Image set
                * */
                val currentFolderId = getItem(position)
                val data: ImageData? = helpingMethodsUtils?.getImageByAlbum(currentFolderId)?.get(0)
                glide.load(data?.imagePath).into(holder.binding.imageView1)
                holder.bind(data)
                holder.binding.cbSelect.isChecked = currentFolderId == helpingMethodsUtils?.getSelectedFolderId()

                holder.binding.clickableView.setOnClickListener {
//                    CURRENT_FOLDER_ID = currentFolderId
                    helpingMethodsUtils?.setSelectedFolderId(currentFolderId)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return folderId?.size!!
    }


    inner class SmallVerticalThumbsVH constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var binding = ItemsBinding.bind(itemView)

        fun bind(item: ImageData?) {
            item?.let {
                binding.textView1.isSelected = true
                binding.textView1.text = item.folderName
                binding.clickableView.setOnClickListener {
                    onItemClickListener?.invoke(adapterPosition, item)
                }

            }


        }


    }

    private var onItemClickListener: ((position: Int, ImageData?) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, ImageData?) -> Unit) {
        onItemClickListener = listener
    }
//
//    interface Interaction {
//        fun onItemSelected(position: Int, item: ImageData?)
//    }


}

