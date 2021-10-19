package com.numan.videoeditor.utils.helping_methods

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.numan.videoeditor.R
import com.numan.videoeditor.db.dataclass.ImageData
import com.numan.videoeditor.db.dataclass.MusicData
import com.numan.videoeditor.extensions.Extensions
import com.numan.videoeditor.utils.Constants
import com.numan.videoeditor.utils.Share
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class HelpingMethodsUtils(private var app: Context) {
    var TwoDServiceisBreak = false
    var ThreeDServiceisBreak = false
    var MoreServiceisBreak = false
    var threeD_Service_On_Off_Flag = false
    var towD_Service_On_Off_Flag = false
    var More_Service_On_Off_Flag = false
    var Save_Service_On_Off_Flag = false
    var changeBackground_Flag = false
    var no_music_command = false
    var frame_command = false
    var error_in_save_video = false

    var isEditModeEnable = false
    var isFromSdCardAudio = false

    private var allFolder: ArrayList<String>? = null
    private var frame = -1
    private var bg = -1
    var startBgSlide = -1
    var endBgSlide = -1
    private var file: File? = null
    var min_pos = Int.MAX_VALUE
    var second = 2.0f
    var selectedFolderId: String? = null

    var videoImages: ArrayList<String>? = ArrayList<String>()
    private var selectedImages: ArrayList<ImageData>? = ArrayList<ImageData>()
    var org_selectedImages: ArrayList<ImageData>? = ArrayList<ImageData>()
    var temp_org_selectedImages: ArrayList<ImageData>? = ArrayList<ImageData>()
    private var allAlbum = HashMap<String?, ArrayList<ImageData?>>()
    var musicData: MusicData? = null

//    var selectedTheme: THREEDTHEMES = THREEDTHEMES.Shine
//    var selectedTheme2d: THEMES2D = THEMES2D.Shine2d
//    var selectedThemeOther: MORETHEME = MORETHEME.MORE


    @JvmName("setMusicData1")
    fun setMusicData(musicData: MusicData?) {
        isFromSdCardAudio = false
        this.musicData = musicData
    }

    fun clearAllSelection() {
        videoImages?.clear()
        getSelectedImages()?.clear()
        getOrgSelectedImages()?.clear()
        getTempOrgSelectedImages()?.clear()
        System.gc()
        getFolderList()
    }


    fun get_overlay(bottom: Bitmap): Bitmap? {
        return try {
            var drawable_overlay: Drawable? = ResourcesCompat.getDrawable(
                app.resources,
                R.drawable.threedframe,
                null
            )
            val b = (drawable_overlay as BitmapDrawable).bitmap
            val bitmapResized = Bitmap.createScaledBitmap(
                b,
                Constants.VIDEO_WIDTH,
                Constants.VIDEO_HEIGHT,
                false
            )
            drawable_overlay = BitmapDrawable(
                app.resources, bitmapResized
            )
            val drawable: Drawable = BitmapDrawable(
                app.resources, getRoundedCornerBitmap(bottom, 35)
            )
            val layers = arrayOfNulls<Drawable>(2)
            layers[0] = drawable
            layers[1] = drawable_overlay
            val layerDrawable = LayerDrawable(layers)
            val width = layerDrawable.intrinsicWidth
            val height = layerDrawable.intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)
            bitmap
        } catch (ignored: Exception) {
            null
        }
    }


    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        return try {
            val output        = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas        = Canvas(output)
            val color         = -0xbdbdbe
            val paint         = Paint()
            val rect          = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF         = RectF(rect)
            val roundPx       = pixels.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color        = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode     = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            output
        } catch (ignored: Exception) {
            null
        }
    }

    fun getFolderList() {
        allFolder         = ArrayList<String>()
        allAlbum          = HashMap<String?, ArrayList<ImageData?>>()
        val projection    = arrayOf("_data", "_id", "bucket_display_name", "bucket_id", "datetaken")
        val orderBy       = "bucket_display_name"
        val cur : Cursor? = app.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "bucket_display_name DESC"
        )
        if (cur?.count == 0) {
            Extensions.logInfo("cur : ${cur.count}")
            Share.image_not_found = cur.count
        } else {
            if (cur?.moveToFirst() == true) {
                val bucketColumn     = cur.getColumnIndex("bucket_display_name")
                val bucketIdColumn   = cur.getColumnIndex("bucket_id")
                val dateColumn       = cur.getColumnIndex("datetaken")

                setSelectedFolderId(cur.getString(bucketIdColumn))

                do {
                    val data = ImageData()
                    data.imagePath          = cur.getString(cur.getColumnIndex("_data"))
                    data.temp_imagePath     = cur.getString(cur.getColumnIndex("_data"))
                    data.temp_org_imagePath = cur.getString(cur.getColumnIndex("_data"))

                    if (!data.imagePath!!.endsWith(".gif")) {

                        val date       = cur.getString(dateColumn)
                        val folderName = cur.getString(bucketColumn)
                        val folderId   = cur.getString(bucketIdColumn)

                        if (!allFolder!!.contains(folderId)) {
                            allFolder?.add(folderId)
                        }
                        val imagePath: ArrayList<ImageData?> = allAlbum.get(folderId)?: ArrayList()

                        data.folderName = folderName
                        imagePath.add(data)
//                        allAlbum.clear()
                        allAlbum.put(folderId, imagePath)
                        Extensions.logInfo("allAlbum Helping: $allAlbum")

//                        Extensions.logInfo("after check path Helping: ${allAlbum[folderId]}")
                    }
                } while (cur.moveToNext())
            }
        }
        cur?.close()
        Extensions.logInfo("Album Helping: ${allAlbum.size}")
        Extensions.logInfo("Folder Helping: ${allFolder?.size}")
    }


    fun getAllAlbum(): HashMap<String?, ArrayList<ImageData?>> {
        return allAlbum
    }



    fun getImageByAlbum(folderId: String?): ArrayList<ImageData?> {
        Log.d("chk", "imageDatas : ${getAllAlbum().get(folderId)}")
        return getAllAlbum().get(folderId)?: ArrayList()
    }

    fun addSelectedImage(imageData: ImageData?) {
//        Extensions.logInfo("imagePath : ${imageData?.imagePath}")
        if (Share.end_slide_selected_or_not) {
            selectedImages?.add(selectedImages!!.size - 1, imageData!!)
            org_selectedImages?.add(org_selectedImages!!.size - 1, imageData!!)
            temp_org_selectedImages!!.add(temp_org_selectedImages!!.size - 1, imageData!!)
            imageData.imageCount++
        } else {
            selectedImages?.add(imageData!!)
            org_selectedImages?.add(imageData!!)
            temp_org_selectedImages?.add(imageData!!)
            imageData!!.imageCount++
        }
    }

    fun removeSelectedImage(imageData: Int) {
        if (imageData <= selectedImages!!.size) {
            Extensions.logInfo("image imagePath : ==> ${selectedImages!![imageData].imagePath}")
            org_selectedImages?.removeAt(imageData)
            temp_org_selectedImages?.removeAt(imageData)
            selectedImages?.removeAt(imageData)!!.imageCount--
        }
    }


    fun getSelectedImages(): ArrayList<ImageData>? {
        return selectedImages
    }



    @JvmName("setSelectedFolderId1")
    fun setSelectedFolderId(selectedFolderId_: String?) {
//        Extensions.logInfo("selectedFolderId Helping: $selectedFolderId")
        selectedFolderId = selectedFolderId_
    }

    @JvmName("getSelectedFolderId1")
    fun getSelectedFolderId(): String? {
        return selectedFolderId
    }


    @JvmName("getMusicData1")
    fun getMusicData(): MusicData? {
        return musicData
    }

    @JvmName("getSecond1")
    fun getSecond(): Float {
        return second
    }
    @JvmName("setSecond1")
    fun setSecond(second: Float) {
        this.second = second
    }


    fun getApplicationTypeFace(): Typeface? {
        return null
    }


//    fun setCurrentTheme(currentTheme: String?) {
//        val editor = getSharedPreferences("theme", 0).edit()
//        editor.putString("current_theme", currentTheme)
//        editor.commit()
//    }

    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        for (service in (context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun setFrame(data: Int) {
        this.frame = data
    }

    fun getFrame(): Int {
        return this.frame
    }

    fun setBg(data: Int) {
        this.bg = data
    }

    @JvmName("getStartBgSlide1")
    fun getStartBgSlide(): Int {
        return startBgSlide
    }

    @JvmName("setStartBgSlide1")
    fun setStartBgSlide(startBgSlide: Int) {
        this.startBgSlide = startBgSlide
    }

    @JvmName("getEndBgSlide1")
    fun getEndBgSlide(): Int {
        return endBgSlide
    }

    @JvmName("setEndBgSlide1")
    fun setEndBgSlide(endBgSlide: Int) {
        this.endBgSlide = endBgSlide
    }

    fun getBg(): Int {
        return this.bg
    }

    fun setEffect(data: File) {
        this.file = data
    }

    fun getEffect(): File? {
        return this.file
    }

    fun initArray() {
        this.videoImages = ArrayList<String>()
    }


    fun getOrgSelectedImages(): ArrayList<ImageData>? {
        return org_selectedImages
    }

    fun getTempOrgSelectedImages(): ArrayList<ImageData>? {
        return temp_org_selectedImages
    }

}