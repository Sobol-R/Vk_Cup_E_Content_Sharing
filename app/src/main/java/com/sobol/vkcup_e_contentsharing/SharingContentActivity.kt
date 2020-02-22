package com.sobol.vkcup_e_contentsharing

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.support.v4.app.ActivityCompat
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import java.io.FileNotFoundException
import android.net.Uri
import android.provider.Settings
import android.support.design.widget.Snackbar

class SharingContentActivity : AppCompatActivity() {

    private var popUpContainer: PopUpContainerView? = null

    private val PICKED_IMAGE_RESULT = 111
    private val STORAGE_PERMISSION_REQUEST = 112

    private var isOpenShareView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpStatusBar()
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            showPermission()
        }
        //VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
    }

    private fun getGalleryImage() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICKED_IMAGE_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICKED_IMAGE_RESULT) {
                try {
                    val imageUri = data!!.data
                    val path = ImageFilePath.getPath(this, imageUri!!)
                    val imageStream = contentResolver.openInputStream(imageUri)
                    val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
                    val processedBitmap = AndroidUtils.processImageBitmapRotation(path!!, selectedImageBitmap)
                    openShareView(processedBitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            val callback = object: VKAuthCallback {
                override fun onLogin(token: VKAccessToken) {
                    println("user passed authorization")
                }

                override fun onLoginFailed(errorCode: Int) {
                    println("FAIL, error code == $errorCode")
                }
            }
            if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
                super.onActivityResult(requestCode, resultCode, data)
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGalleryImage()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showPermission()
                } else {
                    val snackbar = Snackbar.make(
                        content,
                        resources.getString(R.string.message_no_storage_permission_snackbar),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction(resources.getString(R.string.settings)) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    snackbar.show()
                }
            }
        }
    }

    private fun showPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST)
    }

    private fun openShareView(bitmap: Bitmap) {
        isOpenShareView = true
        popUpContainer = PopUpContainerView(this)
        popUpContainer!!.init(bitmap)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        )
        popUpContainer!!.layoutParams = layoutParams
        content.addView(popUpContainer)
        popUpContainer!!.appear()
    }

    fun closeShareView() {
        content.removeView(popUpContainer)
        popUpContainer = null
    }

    fun setUpStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            changeStatusBarColor(ContextCompat.getColor(this, R.color.colorTextDark))
        else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            changeStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        }
    }

    fun setShareStatusBar() {
        window.decorView.systemUiVisibility = 0
        changeStatusBarColor(ContextCompat.getColor(this, R.color.colorShareStatus))
    }

    private fun changeStatusBarColor(newColor: Int) {
        val animator = ValueAnimator.ofArgb(window.statusBarColor, newColor)
        animator.addUpdateListener {
            window.statusBarColor = it.animatedValue as Int
        }
        animator.duration = 150
        animator.start()
    }

    override fun onBackPressed() {
        if (isOpenShareView) {
            isOpenShareView = false
            popUpContainer!!.closeShareView()
        } else
            super.onBackPressed()
    }

}
