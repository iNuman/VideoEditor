package com.numan.videoeditor.extensions

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.numan.videoeditor.BuildConfig
import com.numan.videoeditor.extensions.Extensions.toastFrag
import com.sdsmdg.tastytoast.TastyToast
import timber.log.Timber

object Extensions {


    fun Fragment.toastFrag(msg: String, len: Int = Toast.LENGTH_SHORT) {
        requireContext().toast(msg, len)
    }

    fun Context.toast(msg: String, len: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, len).show()
    }


    fun logInfo(msg: String) {
        Timber.tag("ffnet: 🌀❤💕😎🤷  :: ‍").i(msg)
    }
    fun logError(msg: String) {
        Timber.tag("ffnet: 🌀❤💕😎🤷  :: ‍").e(msg)
    }

    fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            logInfo("Timber is initialised")
        } else {
            logError("You should not be seeing this!")
        }
    }
}
