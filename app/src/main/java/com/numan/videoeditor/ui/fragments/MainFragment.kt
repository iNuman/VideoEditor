package com.numan.videoeditor.ui.fragments

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.numan.videoeditor.R
import com.numan.videoeditor.view_pager_banner_preview.BanerPreview
import com.numan.videoeditor.adapter.BannerAdapterAdapter
import com.numan.videoeditor.databinding.FragmentMainBinding
import com.numan.videoeditor.db.BannerData
import com.numan.videoeditor.di.NetworkHelper
import com.numan.videoeditor.extensions.Extensions.toastFrag
import com.numan.videoeditor.ui.dialog_fragments.ActionsToBePerformedDialogFragment
import com.numan.videoeditor.utils.Constants
import com.numan.videoeditor.utils.Constants.ACTION_SELECTION_DIALOG_TAG
import com.numan.videoeditor.utils.Constants.REQUEST_CODE_PERMISSION
import com.numan.videoeditor.utils.EditorUtilities
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


/*
* https://developer.android.com/topic/libraries/architecture/adding-components
* */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewPager: BannerViewPager<BannerData, BannerAdapterAdapter.NetViewHolder>

    private var shortAnimationDuration: Int = 0
    private lateinit var navController: NavController

    @Inject
    lateinit var helpingMethodsUtils: HelpingMethodsUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        screenRotationDialogStateHandling(savedInstanceState)
        /*
         * Getting Whole Image Folders From Storage
         * */
        helpingMethodsUtils.getFolderList()

        initializations(view)
        setTextsCalls()
        initOnClickListeners()
        crossFade()


    }

    private fun initializations(view: View) {
        mViewPager = view.findViewById(R.id.banner_view)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        navController = Navigation.findNavController(view)
    }


    private fun setupViewPager() {

        mViewPager.apply {
            adapter = BannerAdapterAdapter()
            setAutoPlay(true)
            setLifecycleRegistry(lifecycle)
            setScrollDuration(1000)
            setRevealWidth(32, 32)
            setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            setIndicatorSliderGap(resources.getDimensionPixelOffset(R.dimen.dp_4_5))
            setIndicatorMargin(
                0,
                resources.getDimension(R.dimen.dp_150).toInt(),
                0,
                resources.getDimension(R.dimen.dp_100).toInt()
            )
            setIndicatorStyle(IndicatorStyle.DASH)
            setIndicatorSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorSliderRadius(
                resources.getDimension(R.dimen.dp_3).toInt(),
                resources.getDimension(R.dimen.dp_4_5).toInt()
            )
            setIndicatorSliderColor(
                ContextCompat.getColor(requireContext(), R.color.white),
                ContextCompat.getColor(requireContext(), R.color.colorGrey)
            )
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                    toastFrag("position:$position", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                }
            })
        }.create(BanerPreview.BANNER_DATA)
    }

    private fun setTextsCalls() {
        binding.tv.text = getString(R.string.app_name_here)
        binding.buttonsInclude.editorFab.setOnClickListener(this)
    }

    private fun screenRotationDialogStateHandling(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val testDialog = requireActivity().supportFragmentManager.findFragmentByTag(
                ACTION_SELECTION_DIALOG_TAG
            ) as ActionsToBePerformedDialogFragment?
            testDialog?.setYesListener { action ->
                when (action) {
                    0 -> performSomePositiveAction()
                    else -> performSomeNegativeAction()
                }

            }
        }
    }

    private fun initOnClickListeners() {
        binding.tv.setOnClickListener(this)

    }

    private fun performSomePositiveAction() {
        showToast("Some Positive Action Performed")
    }

    private fun performSomeNegativeAction() {
        showToast("Some Negative Action Performed")
    }

    private fun showToast(msg: String) {
        toastFrag(msg)
    }


    private fun crossFade() {
        var networkHelper: NetworkHelper = NetworkHelper(requireContext())
        if (networkHelper.hasInternetConnection()) {
            binding.cardViewBanner.apply {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                alpha = 0f
                visibility = View.VISIBLE

                // Animate the content view to 100% opacity, and clear any animation
                // listener set on the view.
                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setListener(null)

            }
            setupViewPager()
        } else {
            // Animate the loading view to 0% opacity. After the animation ends,
            // set its visibility to GONE as an optimization step (it won't
            // participate in layout passes, etc.)
            binding.cardViewBanner.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.cardViewBanner.visibility = View.GONE
                    }
                })
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv -> {
                ActionsToBePerformedDialogFragment()
                    .apply {
                        setYesListener { quality ->
                            when (quality) {
                                0 -> {
                                    performSomePositiveAction()
                                }
                                1 -> {
                                    performSomeNegativeAction()
                                }
                            }
                        }
                    }.show(requireActivity().supportFragmentManager, ACTION_SELECTION_DIALOG_TAG)
            }
            R.id.editor_fab -> {
                navController.navigate(R.id.action_global_createVideoFragment)
            }

        }

    }


}