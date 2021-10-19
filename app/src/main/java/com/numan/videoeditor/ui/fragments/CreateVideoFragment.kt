package com.numan.videoeditor.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.numan.videoeditor.R
import com.numan.videoeditor.adapter.AlbumAdapterById
import com.numan.videoeditor.adapter.ImageByAlbumAdapter
import com.numan.videoeditor.adapter.SelectedImageAdapter
import com.numan.videoeditor.custom_views.EmptyRecyclerView
import com.numan.videoeditor.custom_views.ExpandIconView
import com.numan.videoeditor.custom_views.VerticalSlidingPanel
import com.numan.videoeditor.databinding.FragmentVideoEditorBinding
import com.numan.videoeditor.extensions.Extensions.toastFrag
import com.numan.videoeditor.ui.dialog_fragments.ActionsToBePerformedDialogFragment
import com.numan.videoeditor.utils.Constants
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/*
* https://developer.android.com/topic/libraries/architecture/adding-components
* */
@AndroidEntryPoint
class CreateVideoFragment : Fragment(R.layout.fragment_video_editor),
    VerticalSlidingPanel.PanelSlideListener, View.OnClickListener {


    @Inject
    lateinit var helpingMethodsUtils: HelpingMethodsUtils


    private var ivNext: ImageView? = null
    private var tvCountImages: TextView? = null
    private var expandIcon: ExpandIconView? = null

    var isFromPreview = false
    var isPause = false
    private var parent: View? = null

    private var llDelete: ImageView? = null
    private var llEdit: LinearLayout? = null
    var cameraBtn: LinearLayout? = null

    private var rvAlbum: RecyclerView? = null
    private var rvAlbumImages: RecyclerView? = null
    private var rvSelected: RecyclerView? = null
    private var rvSelectedImage: EmptyRecyclerView? = null

    @Inject
    lateinit var albumAdapter: AlbumAdapterById

    @Inject
    lateinit var albumImagesAdapter: ImageByAlbumAdapter

    @Inject
    lateinit var selectedImageAdapter: SelectedImageAdapter
//    private val imageHorizontalAdapter: SelectedImageHorizontalAdapter? = null


    private var panel: VerticalSlidingPanel? = null
    private var tvImageCount: TextView? = null


    val REQUEST_TAKE_PHOTO = 1


    private var _binding: FragmentVideoEditorBinding? = null
    private val binding get() = _binding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVideoEditorBinding.bind(view)

//        screenRotationDialogStateHandling(savedInstanceState)

//        setTextsCalls()
//        initOnClickListeners()
        bindViews(view)
        recyclerViewSetups()
        initOnClickListeners()

    }

    private fun bindViews(view: View) {

        tvImageCount        =  binding?.overviewPanelInclude?.tvImageCount
        expandIcon          =  binding?.overviewPanelInclude?.settingsDragArrow

        parent              =  binding?.overviewPanelInclude?.defaultHomeScreenPanel
        llDelete            =  binding?.overviewPanelInclude?.deleteIv
        ivNext              =  binding?.customToolbarInclude?.imgViewNext

        rvAlbum             = binding?.overviewPanelInclude?.rvAlbum
        rvAlbumImages       = binding?.overviewPanelInclude?.rvImageAlbum
        rvSelectedImage     = binding?.overviewPanelInclude?.rvSelectedImagesList

        panel               = binding?.overviewPanelInclude?.slidingLayouts
        panel?.setEnableDragViewTouchEvents(true)
        panel?.setDragView(binding?.overviewPanelInclude?.settingsPaneHeader)


//        rvSelected = binding.overviewPanelInclude.rvSelectedImagesList
//        btnClear = findViewById<Button>(R.id.btnClear)
//        tvCountImages = findViewById<TextView>(R.id.tvCountImages)
//        llEdit = findViewById<LinearLayout>(R.id.llEdit)


    }

    private fun recyclerViewSetups() {


        albumAdapter.AlbumAdapterById(requireContext())
//     Recyclerview albums
        rvAlbum?.apply {
            layoutManager  = LinearLayoutManager(requireContext())
            itemAnimator   = DefaultItemAnimator()
            adapter        = albumAdapter
        }

//     Recyclerview images  on right side of pickUp ImageView activity
        rvAlbumImages?.apply {
            layoutManager   = GridLayoutManager(requireContext(), 2)
            itemAnimator    = DefaultItemAnimator()
            adapter         = albumImagesAdapter
        }

//     Recyclerview selected  at bottom of pickUp ImageView activity
        rvSelectedImage?.apply {
            layoutManager   = GridLayoutManager(requireContext(), 4)
            itemAnimator    = DefaultItemAnimator()
            adapter         = selectedImageAdapter
            setEmptyView(binding?.overviewPanelInclude?.listEmpty)
        }

/*
        albumAdapter.setOnItemClickListener { position, imageData ->
            showToast(" id check: $CURRENT_FOLDER_ID")
            helpingMethodsUtils.setSelectedFolderId(CURRENT_FOLDER_ID)
            Extensions.logInfo("getSelectedFolder Create Video Album Adapter click: ${helpingMethodsUtils.getSelectedFolderId()}")
            albumAdapter.notifyDataSetChanged()

        }*/


        albumImagesAdapter.setOnItemClickListener { position, imageData ->
   
//            Extensions.logInfo("getSelectedFolder: ${helpingMethodsUtils.getSelectedFolderId()}")
            albumImagesAdapter.notifyDataSetChanged()
        }


        /*
* Remove Image From Horizontal Adapter
* */
        selectedImageAdapter.setOnItemClickListener { position, imageData ->
            toastFrag("Removed")
            val index: Int = helpingMethodsUtils.getSelectedImages()?.indexOf(imageData)!!
            if (isFromPreview) {
                helpingMethodsUtils.min_pos = helpingMethodsUtils.min_pos.coerceAtMost(0.coerceAtLeast(position - 1))
            }
            helpingMethodsUtils.removeSelectedImage(position)
            selectedImageAdapter.notifyItemRemoved(position)
        }

//        selectedImageAdapter.setOnItemClickListener { position, imageData ->
//
//            selectedImageAdapter.notifyDataSetChanged()
//        }


        tvImageCount?.text =
            java.lang.String.valueOf(helpingMethodsUtils.getOrgSelectedImages()?.size)
//        tvCountImages?.text = java.lang.String.valueOf(helpingMethodsUtils.getOrgSelectedImages()?.size)
    }

    private fun initOnClickListeners() {

//        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom)
//        ivNext?.startAnimation(animation)
        ivNext?.setOnClickListener(this)
        llDelete?.setOnClickListener(this)
        llEdit?.setOnClickListener(this)
        panel?.setPanelSlideListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.delete_iv -> {
                clearAllDialog()
            }

            R.id.img_view_next -> {
                showToast("Will add this later on")
            }

        }

    }

    private fun clearAllDialog() {
        ActionsToBePerformedDialogFragment()
            .apply {
                setYesListener { quality ->
                    when (quality) {
                        0 -> {
                            clearData()
                            showToast("Cleared")
                        }
                        1 -> {
                            showToast("Cancelled")
                        }
                    }
                }
            }.show(requireActivity().supportFragmentManager, Constants.ACTION_SELECTION_DIALOG_TAG)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        if (expandIcon != null) {
            expandIcon?.setFraction(slideOffset, false)
        }
        if (slideOffset >= 0.005f) {
            if (parent != null && parent?.visibility != View.VISIBLE) {
                parent?.visibility = View.VISIBLE
            }
        } else if (parent != null && parent?.visibility == View.VISIBLE) {
            parent?.visibility = View.GONE
        }
    }

    override fun onPanelCollapsed(panel: View?) {
        if (parent != null) {
            parent?.visibility = View.VISIBLE
        }
        selectedImageAdapter.isExpanded = false
        selectedImageAdapter.notifyDataSetChanged()
    }

    override fun onPanelExpanded(panel: View?) {
        if (parent != null) {
            parent?.visibility = View.GONE
        }
        selectedImageAdapter.isExpanded = true
        selectedImageAdapter.notifyDataSetChanged()
    }

    override fun onPanelAnchored(panel: View?) {}

    override fun onPanelShown(panel: View?) {}

    private fun clearData() {
        for (i in helpingMethodsUtils.getSelectedImages()?.size?.minus(1)?.downTo(0)!!) {
            helpingMethodsUtils.removeSelectedImage(i)
        }
        tvCountImages?.text = "0"
        llDelete?.isEnabled = false
        llDelete?.alpha = 0.5f
        selectedImageAdapter.notifyDataSetChanged()
        albumImagesAdapter.notifyDataSetChanged()
    }


    private fun showToast(msg: String) {
        toastFrag(msg)
    }


}