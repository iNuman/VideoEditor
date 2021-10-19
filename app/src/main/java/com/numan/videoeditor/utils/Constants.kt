package com.numan.videoeditor.utils

import android.Manifest

object Constants {
    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    const val EXTRA_FROM_PREVIEW = "extra_from_preview"
    const val REQUEST_CODE_PERMISSION = 0
    const val SHARED_PREFERENCES_NAME = "shared_prefs"
    const val KEY_DIALOG = "key_dialog"
    const val ACTION_SELECTION_DIALOG_TAG = "action_selection_dialog"
    const val VIDEO_HEIGHT = 480
    const val VIDEO_WIDTH = 720
    var CURRENT_FOLDER_ID: String? = null
}