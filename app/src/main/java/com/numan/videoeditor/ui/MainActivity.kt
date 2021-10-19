package com.numan.videoeditor.ui

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.numan.videoeditor.databinding.ActivityMainBinding
import com.numan.videoeditor.utils.Constants.PERMISSIONS
import com.numan.videoeditor.utils.Constants.REQUEST_CODE_PERMISSION
import com.numan.videoeditor.utils.EditorUtilities
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
//    @Inject
//    lateinit var helpingMethodsUtils: HelpingMethodsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissions()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = ContextCompat.getColor(this@DashBoardActivity, R.color.colorPrimary)
//        }

        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#151515")
        }


    }

    private fun requestPermissions() {
        if (EditorUtilities.hasPermissions(this, PERMISSIONS)) {
            /*
            * Getting Whole Image Folders From Storage
            * */
//            helpingMethodsUtils.getFolderList()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept Read/Write permissions to use this app.",
                REQUEST_CODE_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty()){
            /*
            * Getting Whole Image Folders From Storage
            * */
//            helpingMethodsUtils.getFolderList()
        }
    }
}