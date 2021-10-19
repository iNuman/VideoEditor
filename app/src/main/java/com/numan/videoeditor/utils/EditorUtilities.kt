package com.numan.videoeditor.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

object EditorUtilities {

    fun hasPermissions(
        context: Context?,
        permissions: Array<String>
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            permissions.iterator().forEach {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }



}