package com.numan.videoeditor.utils

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.numan.videoeditor.R
import com.numan.videoeditor.db.dataclass.ImageData
import com.numan.videoeditor.db.dataclass.TEXT_MODEL
import java.util.*
import javax.inject.Inject

object Share  {
    var FirstTimePreviewActivity = false
    var isPreviewActivity = false
    var TwoDEffectPreviewActivity = false
    var MoreEffectPreviewActivity = false
    var Effect_Flag = false
    var save_video_Flag = false
    var twoD_Effect_Flag = false
    var threeD_Effect_Flag = false
    var more_Effect_Flag = false
    var pause_video_flag = false
    var from_my_video = false
    var image_not_found = -1

    // remove effect flag //
    var threeDEffectApply = false
    var twoDEffectApply = false
    var moreEffectApply = false

    // step mark //
    var effect_step_flag = false
    var resume_flag = false
    var plus_button_click_position = 0
    var temp_selected_crop_image_position = 0
    var selected_crop_image_position = 0
    var selected_theme_position = 0
    var height_of_row = 0
    var width_of_row = 0
    var temp_2DEffect_selectedImages: ArrayList<ImageData?> = ArrayList()
    var temp_MoreEffect_selectedImages: ArrayList<ImageData?> = ArrayList()
    var audio_file : String? = null
    var selected_image_path: String? = null
    var selected_image_pos = 0
    var selected_audio_pos = 0
    var video_path: String? = null
    var buffer_progress = 0.0f
    var BG_GALLERY: Uri? = null

    // font sticker
    var TEXT: String? = null
    var FONT_STYLE: String? = null
    var START_TEXT_EDIT_FLAG = false
    var EDIT_DAILOG_DRAWABLE: Drawable? = null
    var tag_value = "text_sticker"
    var START_STICKER_POSITION = 0
    var SYMBOL: Drawable? = null
    var FLAG = false
    var FONT_EFFECT = "6"
    var COLOR = Color.parseColor("#000000")
    var FONT_TEXT_DRAWABLE: Drawable? = null
    var COLOR_POS = 0
    var START_FONT_TEXT2 = ArrayList<TEXT_MODEL>()
    var TEXT_EDIT_FLAG = false
    var STICKER_POSITION = 0
    var FONT_TEXT2 = ArrayList<TEXT_MODEL>()

    // gallery flag //
    var FROM_CUSTOM_SLIDE = false
    var GALLERY_BITMAP: Bitmap? = null

    // slide //
    var end_slide_selected_or_not = false

    /*
    * TODO: SHARE: TO ADD BUY PRO-VERSION/ AD FREE VERSION 
    * */
    // for in-app purchase
    fun isNeedToAdShow(context: Context?): Boolean {

//        if (!SharedPrefs.contain(context, SharedPrefs.IS_ADS_REMOVED)) {
//            isNeedToShow = true;
//        } else {
//            if (!SharedPrefs.getBoolean(context, SharedPrefs.IS_ADS_REMOVED))
//                isNeedToShow = true;
//            else
//                isNeedToShow = false;
//        }
        return true
    }

    fun showAlert(activity: Activity?, title: String?, message: String?) {
        val builder = AlertDialog.Builder(
            activity!!, R.style.AppCompatAlertDialogStyle
        )
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.show()
    }

    fun createProgressDialog(mContext: Context?): ProgressDialog {
        val dialog = ProgressDialog(mContext, R.style.MyTheme)
        try {
            if (dialog.isShowing) {
                dialog.dismiss()
                createProgressDialog(mContext)
            } else {
                dialog.show()
            }
        } catch (e: Exception) {
//			Log.e("dialog crash","crash"+e.getMessage());
            e.printStackTrace()
        }
        //dialog.setCancelable(true);
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setProgressDrawable(ColorDrawable(Color.parseColor("#E45E46")))
        dialog.setContentView(R.layout.progress_dialog_layout)
        return dialog
    }

    fun RestartAppForOnlyStorage(activity: Activity): Boolean {
        return if (!checkAndRequestPermissionsForStorage(activity, 1)) {
            resume_flag = true
            val i = activity.baseContext.packageManager
                .getLaunchIntentForPackage(activity.baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.startActivity(i)
            false
        } else {
            true
        }
    }

    private fun checkAndRequestPermissionsForStorage(act: Activity?, code: Int): Boolean {
        return !(ContextCompat.checkSelfPermission(
            act!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    act,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED)
    }

    fun RestartAppStorage(activity: Activity): Boolean {
        return if (!checkAndRequestPermissions(activity, 1)) {
            resume_flag = true
            val i = activity.baseContext.packageManager
                .getLaunchIntentForPackage(activity.baseContext.packageName)
            i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity.startActivity(i)
            false
        } else {
            true
        }
    }

    private fun checkAndRequestPermissions(act: Activity?, code: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(
                act!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            false
        } else ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    } //    getIntent().getExtras() != null && getIntent().getExtras().containsKey("avairy")
}