package com.numan.videoeditor.db.dataclass

import android.graphics.drawable.Drawable
import android.text.TextUtils

class ImageData {
    var folderName: String? = null
    var id: Long = 0
    var imageAlbum: String? = null
    var imageCount :Int = 0
    var imagePath: String? = null
    var temp_imagePath: String? = null
    var temp_org_imagePath: String? = null
    var twoDEffect = 0
    var drawable: Drawable? = null
    var direction: String? = null
    var direction_position = 0
    var moredirection: String? = null
    var more_direction_position = 0
    var selected_image_count = 0
    var isSupported = true
    override fun toString(): String {
        return if (TextUtils.isEmpty(imagePath)) {
            super.toString()
        } else "ImageData { imagePath=$imagePath,folderName=$folderName,imageCount=$imageCount }"
    }
}