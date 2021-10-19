package com.numan.videoeditor.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.numan.videoeditor.extensions.Extensions
import java.io.*
import java.util.*

object Util {
    var backgrnd: Bitmap? = null
    fun isDebug(context: Context): Boolean {
        return 0 != ApplicationInfo.FLAG_DEBUGGABLE.let {
            context.applicationContext.applicationInfo.flags =
                context.applicationContext.applicationInfo.flags and it; context.applicationContext.applicationInfo.flags
        }
    }

    fun close(inputStream: InputStream?) {
        if (inputStream != null) {
            try {
                inputStream.close()
            } catch (e: IOException) {
                // Do nothing
            }
        }
    }

    @JvmStatic
    fun close(outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                outputStream.flush()
                outputStream.close()
            } catch (e: IOException) {
                // Do nothing
            }
        }
    }

    fun convertInputStreamToString(inputStream: InputStream?): String? {
        try {
            val r = BufferedReader(InputStreamReader(inputStream))
            var str: String?
            val sb = StringBuilder()
            while (r.readLine().also { str = it } != null) {
                sb.append(str)
            }
            return sb.toString()
        } catch (e: IOException) {
            Extensions.logError("error converting ${e.message}")
        }
        return null
    }

    fun destroyProcess(process: Process?) {
        process?.destroy()
    }

    fun killAsync(asyncTask: AsyncTask<*, *, *>?): Boolean {
        return asyncTask != null && !asyncTask.isCancelled && asyncTask.cancel(true)
    }

    fun isProcessCompleted(process: Process?): Boolean {
        try {
            if (process == null) return true
            process.exitValue()
            return true
        } catch (e: IllegalThreadStateException) {
            // do nothing
        }
        return false
    }
}