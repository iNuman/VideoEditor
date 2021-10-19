package com.numan.videoeditor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.numan.videoeditor.extensions.Extensions
import com.numan.videoeditor.utils.Util.close
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

class FileUtils {
    class C06661(  /* synthetic */private val `val$child`: File) : Thread() {
        override fun run() {
            deleteFile(`val$child`)
        }
    }

    companion object {
        var mSdCard: File? = File(Environment.getExternalStorageDirectory().absolutePath)

        // media/audio/audioVideoMerge/
        var APP_DIRECTORY = File(mSdCard, "VideoEditorKatoKato")
        var SAVE_DIRECTORY = File(mSdCard, "media/video/GeneratedVideos/")
        private const val DEFAULT_BUFFER_SIZE = 4096
        private const val EOF = -1
        val TEMP_DIRECTORY = File(APP_DIRECTORY, ".temp")
        val TEMP_DIRECTORY_IMAGES = File(APP_DIRECTORY, ".temp_images")
        val TEMP_DIRECTORY_IMAGES1 = File(APP_DIRECTORY, ".temp_images1")
        val TEMP_DIRECTORY_IMAGES_CROP = File(APP_DIRECTORY, ".crop_images")
        val TEMP_DIRECTORY_SLIDE = File(APP_DIRECTORY, ".temp_slide")
        val TEMP_DIRECTORY_AUDIO = File(APP_DIRECTORY, ".temp_audio")
        val TEMP_VID_DIRECTORY = File(TEMP_DIRECTORY, ".temp_vid")
        const val ffmpegFileName = "ffmpeg"
        val frameFile = File(APP_DIRECTORY, ".frame.png")
        const val hiddenDirectoryName = ".KatoKatoMyGalaryLock/"
        const val hiddenDirectoryNameImage = "Image/"
        const val hiddenDirectoryNameThumbImage = ".thumb/Image/"
        const val hiddenDirectoryNameThumbVideo = ".thumb/Video/"
        const val hiddenDirectoryNameVideo = "Video/"
        var mDeleteFileCount: Long = 0
        private var mStorageList: Array<File?>? = null
        val rawExternalStorage = System.getenv("EXTERNAL_STORAGE")
        var rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
        var unlockDirectoryNameImage = "KatoKatoGalaryLock/Image/"
        var unlockDirectoryNameVideo = "KatoKatoGalaryLock/Video/"
        val videoDirectory: File
            get() {
                if (!TEMP_VID_DIRECTORY.exists()) {
                    TEMP_VID_DIRECTORY.mkdirs()
                }
                return TEMP_VID_DIRECTORY
            }

        fun getVideoFile(vNo: Int): File {
            if (!TEMP_VID_DIRECTORY.exists()) {
                TEMP_VID_DIRECTORY.mkdirs()
            }
            return File(
                TEMP_VID_DIRECTORY,
                String.format("vid_%03d.mp4", *arrayOf<Any>(Integer.valueOf(vNo)))
            )
        }

        fun getImageDirectory(iNo: Int): File {
            val imageDir =
                File(TEMP_DIRECTORY, String.format("IMG_%03d", *arrayOf<Any>(Integer.valueOf(iNo))))
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            return imageDir
        }

        fun getImageDirectory(theme: String?): File {
            val imageDir = File(TEMP_DIRECTORY, theme)
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            return imageDir
        }

        fun getImageDirectory(theme: String?, iNo: Int): File {
            val imageDir = File(
                getImageDirectory(theme), String.format(
                    "IMG_%03d", *arrayOf<Any>(
                        Integer.valueOf(iNo)
                    )
                )
            )
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            return imageDir
        }

        fun deleteThemeDir(theme: String?): Boolean {
            return deleteFile(getImageDirectory(theme))
        }

        private val storge: Array<File?>?
            private get() {
                val storage: MutableList<File?>? = null
                if (rawExternalStorage != null) {
                    storage?.add(File(rawExternalStorage))
                } else if (mSdCard != null) {
                    storage?.add(mSdCard!!)
                }
                if (rawSecondaryStoragesStr != null) {
                    storage?.add(File(rawSecondaryStoragesStr))
                }
                mStorageList = arrayOfNulls(storage?.size!!)
                for (i in storage.indices) {
                    mStorageList!![i] = storage[i]
                }
                return mStorageList
            }
        val storages: Array<File?>?
            get() = if (mStorageList != null) {
                mStorageList
            } else storge

        fun humanReadableByteCount(bytes: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (bytes < unit.toLong()) {
                return StringBuilder(bytes.toString()).append(" B").toString()
            }
            val pre = StringBuilder(
                (if (si) "kMGTPE" else "KMGTPE")[(Math.log(
                    bytes.toDouble()
                ) / Math.log(unit.toDouble())).toInt() - 1].toString()
            ).append(if (si) "" else "i").toString()
            return String.format(
                "%.1f %sB", *arrayOf<Any>(
                    java.lang.Double.valueOf(
                        bytes.toDouble() / Math.pow(
                            unit.toDouble(),
                            (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
                                .toDouble()
                        )
                    ), pre
                )
            )
        }

        fun getHiddenAppDirectory(sdCard: File?): File {
            val file = File(sdCard, hiddenDirectoryName)
            if (file.exists()) {
                file.mkdirs()
            }
            return file
        }

        fun getMoveFolderpath(oldFilePath: File, newFolderName: String): File {
            return File(
                oldFilePath.parentFile.parentFile, StringBuilder(
                    newFolderName
                ).append("/").append(oldFilePath.name).toString()
            )
        }

        fun getRestoreVideoDirectory(sdCFile: File?, folderName: String): File {
            return File(sdCFile, unlockDirectoryNameVideo + folderName)
        }

        fun getRestoreImageDirectory(sdCFile: File?, folderName: String): File {
            return File(sdCFile, unlockDirectoryNameImage + folderName)
        }

        fun getHiddenVideoDirectory(sdCFile: File?, folderName: String): File {
            return File(sdCFile, ".MyGalaryLock/Video/$folderName")
        }

        fun getHiddenImageDirectory(sdCFile: File?, folderName: String): File {
            return File(sdCFile, ".MyGalaryLock/Image/$folderName")
        }

        fun getThumbnailDirectory(sdCard: File?, type: Int): File {
            val file: File
            file = if (type == 0) {
                File(
                    getHiddenAppDirectory(sdCard),
                    hiddenDirectoryNameThumbVideo
                )
            } else {
                File(
                    getHiddenAppDirectory(sdCard),
                    hiddenDirectoryNameThumbImage
                )
            }
            if (file.exists()) {
                file.mkdirs()
            }
            return file
        }

        fun getHiddenImageAppDirectory(sdCard: File?): File {
            val file = File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameImage)
            if (file.exists()) {
                file.mkdirs()
            }
            return file
        }

        fun getHiddenVideoAppDirectory(sdCard: File?): File {
            val file = File(getHiddenAppDirectory(sdCard), hiddenDirectoryNameVideo)
            if (file.exists()) {
                file.mkdirs()
            }
            return file
        }

        fun getCacheDirectory(packageName: String): ArrayList<File?> {
            val cacheDirs: ArrayList<File?>? = null
            for (sdCard in storages!!) {
                var file = File(sdCard, "Android/data/$packageName/cache")
                if (file.exists()) {
                    cacheDirs?.add(file)
                }
                file = File(sdCard, "Android/data/$packageName/Cache")
                if (file.exists()) {
                    cacheDirs?.add(file)
                }
                file = File(sdCard, "Android/data/$packageName/.cache")
                if (file.exists()) {
                    cacheDirs?.add(file)
                }
                file = File(sdCard, "Android/data/$packageName/.Cache")
                if (file.exists()) {
                    cacheDirs?.add(file)
                }
            }
            return cacheDirs!!
        }

        @get:SuppressLint("SimpleDateFormat")
        val outputImageFile: File?
            get() = if (!APP_DIRECTORY.exists() && !APP_DIRECTORY.mkdirs()) {
                null
            } else File(
                APP_DIRECTORY,
                "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg"
            )

        fun getPicFromBytes(bytes: ByteArray?): Bitmap? {
            if (bytes == null) {
                return null
            }
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, o)
            var scale = 1
            if (o.outWidth >= o.outHeight) {
                if (o.outWidth >= 1024) {
                    scale = Math.round(o.outWidth.toFloat() / 1024.0f)
                }
            } else if (o.outHeight >= 1024) {
                scale = Math.round(o.outHeight.toFloat() / 1024.0f)
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, o2)
                .copy(Bitmap.Config.RGB_565, true)
        }

        fun getDirectorySize(directory: File?): Long {
            var dirSize: Long = 0
            if (directory != null) {
                val listFile = directory.listFiles()
                if (listFile != null && listFile.size > 0) {
                    for (mFile in listFile) {
                        dirSize += if (mFile.isDirectory) {
                            getDirectorySize(mFile)
                        } else {
                            mFile.length()
                        }
                    }
                }
            }
            return dirSize
        }

        @Throws(IOException::class)
        fun copyFile(src: File?, dst: File?) {
            val `in`: InputStream = FileInputStream(src)
            val out: OutputStream = FileOutputStream(dst)
            val buf = ByteArray(1024)
            while (true) {
                val len = `in`.read(buf)
                if (len <= 0) {
                    `in`.close()
                    out.close()
                    return
                }
                out.write(buf, 0, len)
            }
        }

        @Throws(IOException::class)
        fun copyFileTemp(src: File?, dst: File?) {
            val `in`: InputStream = FileInputStream(src)
            val out: OutputStream = FileOutputStream(dst)
            val buf = ByteArray(1024)
            while (true) {
                val len = `in`.read(buf)
                if (len <= 0) {
                    `in`.close()
                    out.close()
                    return
                }
                out.write(buf, 0, len)
            }
        }

        fun deleteTempDir() {
            try {
                for (child in TEMP_DIRECTORY.listFiles()) {
                    C06661(child).start()
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }

        fun deleteFile(mFile: File?): Boolean {
            var idDelete = false
            if (mFile == null) {
                return false
            }
            if (mFile.exists()) {
                if (mFile.isDirectory) {
                    val children = mFile.listFiles()
                    if (children != null && children.size > 0) {
                        for (child in children) {
                            mDeleteFileCount += child.length()
                            idDelete = deleteFile(child)
                        }
                    }
                    mDeleteFileCount += mFile.length()
                    idDelete = mFile.delete()
                } else {
                    mDeleteFileCount += mFile.length()
                    idDelete = mFile.delete()
                }
            }
            return idDelete
        }

        fun deleteFile(s: String?): Boolean {
            return deleteFile(File(s))
        }

        @Throws(IOException::class)
        fun moveFile(src: File, des: File) {
            if (src.exists() && !src.renameTo(des)) {
                if (!des.parentFile.exists()) {
                    des.parentFile.mkdirs()
                }
                val `in`: InputStream = FileInputStream(src)
                val out: OutputStream = FileOutputStream(des)
                val buf = ByteArray(1024)
                while (true) {
                    val len = `in`.read(buf)
                    if (len <= 0) {
                        break
                    }
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                if (src.exists()) {
                    src.delete()
                }
            }
        }

        @Throws(IOException::class)
        fun moveFile(sourceLocation: String?, targetLocation: String?) {
            if (!File(sourceLocation).renameTo(File(targetLocation))) {
                val file = File(sourceLocation)
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                val `in`: InputStream = FileInputStream(sourceLocation)
                val out: OutputStream = FileOutputStream(targetLocation)
                val buf = ByteArray(1024)
                while (true) {
                    val len = `in`.read(buf)
                    if (len <= 0) {
                        break
                    }
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                val deleteFile = File(sourceLocation)
                if (deleteFile.exists()) {
                    deleteFile.delete()
                }
            }
        }

        fun getFileFormat(id: String): String {
            val format = System.currentTimeMillis().toString() + "_0"
            if (id.endsWith("_1")) {
                return ".jpg"
            }
            if (id.endsWith("_2")) {
                return ".JPG"
            }
            if (id.endsWith("_3")) {
                return ".png"
            }
            if (id.endsWith("_4")) {
                return ".PNG"
            }
            if (id.endsWith("_5")) {
                return ".jpeg"
            }
            if (id.endsWith("_6")) {
                return ".JPEG"
            }
            if (id.endsWith("_7")) {
                return ".mp4"
            }
            if (id.endsWith("_8")) {
                return ".3gp"
            }
            if (id.endsWith("_9")) {
                return ".flv"
            }
            if (id.endsWith("_10")) {
                return ".m4v"
            }
            if (id.endsWith("_11")) {
                return ".avi"
            }
            if (id.endsWith("_12")) {
                return ".wmv"
            }
            if (id.endsWith("_13")) {
                return ".mpeg"
            }
            if (id.endsWith("_14")) {
                return ".VOB"
            }
            if (id.endsWith("_15")) {
                return ".MOV"
            }
            if (id.endsWith("_16")) {
                return ".MPEG4"
            }
            if (id.endsWith("_17")) {
                return ".DivX"
            }
            return if (id.endsWith("_18")) {
                ".mkv"
            } else format
        }

        fun genrateFileId(filePath: String): String {
            try {
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val format = System.currentTimeMillis().toString()
            if (filePath.endsWith(".jpg")) {
                return StringBuilder(format).append("_1").toString()
            }
            if (filePath.endsWith(".JPG")) {
                return StringBuilder(format).append("_2").toString()
            }
            if (filePath.endsWith(".png")) {
                return StringBuilder(format).append("_3").toString()
            }
            if (filePath.endsWith(".PNG")) {
                return StringBuilder(format).append("_4").toString()
            }
            if (filePath.endsWith(".jpeg")) {
                return StringBuilder(format).append("_5").toString()
            }
            if (filePath.endsWith(".JPEG")) {
                return StringBuilder(format).append("_6").toString()
            }
            if (filePath.endsWith(".mp4")) {
                return StringBuilder(format).append("_7").toString()
            }
            if (filePath.endsWith(".3gp")) {
                return StringBuilder(format).append("_8").toString()
            }
            if (filePath.endsWith(".flv")) {
                return StringBuilder(format).append("_9").toString()
            }
            if (filePath.endsWith(".m4v")) {
                return StringBuilder(format).append("_10").toString()
            }
            if (filePath.endsWith(".avi")) {
                return StringBuilder(format).append("_11").toString()
            }
            if (filePath.endsWith(".wmv")) {
                return StringBuilder(format).append("_12").toString()
            }
            if (filePath.endsWith(".mpeg")) {
                return StringBuilder(format).append("_13").toString()
            }
            if (filePath.endsWith(".VOB")) {
                return StringBuilder(format).append("_14").toString()
            }
            if (filePath.endsWith(".MOV")) {
                return StringBuilder(format).append("_15").toString()
            }
            if (filePath.endsWith(".MPEG4")) {
                return StringBuilder(format).append("_16").toString()
            }
            if (filePath.endsWith(".DivX")) {
                return StringBuilder(format).append("_17").toString()
            }
            return if (filePath.endsWith(".mkv")) {
                StringBuilder(format).append("_18").toString()
            } else format
        }

        @SuppressLint("DefaultLocale")
        fun getDuration(duration: Long): String {
            if (duration < 1000) {
                return String.format(
                    "%02d:%02d",
                    *arrayOf<Any>(Integer.valueOf(0), Integer.valueOf(0))
                )
            }
            val n = duration / 1000
            val n2 = n / 3600
            val n4 = n - (3600 * n2 + 60 * ((n - 3600 * n2) / 60))
            return if (n2 == 0L) {
                String.format(
                    "%02d:%02d",
                    *arrayOf<Any>(java.lang.Long.valueOf(0), java.lang.Long.valueOf(n4))
                )
            } else String.format(
                "%02d:%02d:%02d",
                *arrayOf<Any>(
                    java.lang.Long.valueOf(n2),
                    java.lang.Long.valueOf(0),
                    java.lang.Long.valueOf(n4)
                )
            )
        }

        private fun putPrefixZero(n: Long): String {
            return if (n.toString().length < 2) {
                "0$n"
            } else n.toString()
        }

        fun readPatternData(activity: Context): CharArray? {
            try {
                if (File(activity.filesDir.toString() + "/pattern").exists()) {
                    val objectInputStream =
                        ObjectInputStream(FileInputStream(activity.filesDir.toString() + "/pattern"))
                    val array = objectInputStream.readObject() as CharArray
                    objectInputStream.close()
                    return array
                }
            } catch (e: Exception) {
            }
            return null
        }

        fun copyBinaryFromAssetsToData(
            context: Context,
            fileNameFromAssets: String?,
            outputFileName: String?
        ): Boolean {
            val filesDirectory = getFilesDirectory(context)
            try {
                val `is` = context.assets.open(fileNameFromAssets!!)
                val os: OutputStream = FileOutputStream(File(filesDirectory, outputFileName))
                val buffer = ByteArray(4096)
                while (true) {
                    val n = `is`.read(buffer)
                    if (-1 == n) {
                        close(os)
                        close(`is`)
                        return true
                    }
                    os.write(buffer, 0, n)
                }
            } catch (e: IOException) {
                // Log.m21e("issue in coping binary from assets to data. ", e);
                return false
            }
        }

        fun getFilesDirectory(context: Context): File {
            return context.filesDir
        }

        fun getFFmpeg(context: Context): String {
            return StringBuilder(getFilesDirectory(context).absolutePath.toString()).append(File.separator)
                .append(
                    ffmpegFileName
                ).toString()
        }

        fun getFFmpeg(context: Context, environmentVars: Map<String?, String?>?): String {
            var ffmpegCommand = ""
            if (environmentVars != null) {
                for ((key, value) in environmentVars) {
                    ffmpegCommand = StringBuilder(ffmpegCommand).append(key).append("=").append(
                        value
                    ).append(" ").toString()
                }
            }
            return StringBuilder(ffmpegCommand).append(getFFmpeg(context)).toString()
        }

        fun SHA1(file: String?): String? {
            var `is`: InputStream? = null
            try {
                `is` = BufferedInputStream(FileInputStream(file))
                return SHA1(`is`)
            } catch (e: IOException) {
                Extensions.logError("FileUtils ${e.message}")
            } finally {
                close(`is`)
            }
            return null
        }

        fun SHA1(`is`: InputStream): String? {
            try {
                val messageDigest = MessageDigest.getInstance("SHA1")
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    messageDigest.update(buffer, 0, read)
                }
                val formatter = Formatter()
                // Convert the byte to hex format
                for (b in messageDigest.digest()) {
                    formatter.format("%02x", b)
                }
                return formatter.toString()
            } catch (e: NoSuchAlgorithmException) {
                Extensions.logError("FileUtils ${e.message}")
            } catch (e: IOException) {
                Extensions.logError("FileUtils ${e.message}")
            } finally {
                close(`is`)
            }
            return null
        }

        fun addVideoToConcat(i: Int) {
            appendLog(
                File(videoDirectory, "video.txt"), String.format(
                    "file '%s'", *arrayOf<Any>(
                        getVideoFile(i).absolutePath
                    )
                )
            )
        }

        fun addImageTovideo(file: File) {
            appendLog(videoDirectory, String.format("file '%s'", *arrayOf<Any>(file.absolutePath)))
        }

        fun appendLog(parent: File, text: String?) {
            if (!parent.exists()) {
                try {
                    parent.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            try {
                val buf = BufferedWriter(FileWriter(parent, true))
                buf.append(text)
                buf.newLine()
                buf.close()
            } catch (e2: IOException) {
                e2.printStackTrace()
            }
        }

        init {
            if (!TEMP_DIRECTORY.exists()) {
                TEMP_DIRECTORY.mkdirs()
            }
            if (!TEMP_VID_DIRECTORY.exists()) {
                TEMP_VID_DIRECTORY.mkdirs()
            }
        }
    }

    init {
        mDeleteFileCount = 0
    }
}