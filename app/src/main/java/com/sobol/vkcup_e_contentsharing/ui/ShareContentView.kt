package com.sobol.vkcup_e_contentsharing.ui

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import com.sobol.vkcup_e_contentsharing.AndroidUtils
import com.sobol.vkcup_e_contentsharing.R
import com.sobol.vkcup_e_contentsharing.api.VKWallPostCommand
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.share_content_view.view.*

private const val APPEAR_DURATION = 250L

class ShareContentView(
    context: Context
) : FrameLayout(context) {

    private lateinit var container: PopUpContainerView

    private var uri: Uri? = null

    private lateinit var sharingActivity: SharingContentActivity

    init {
        sharingActivity = context as SharingContentActivity
    }

    fun init(container: PopUpContainerView, bitmap: Bitmap, uri: Uri?) {
        this.uri = uri
        this.container = container

        LayoutInflater.from(context).inflate(R.layout.share_content_view, this, true)
        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        params.behavior = BottomSheetBehavior<FrameLayout>()
        layoutParams = params

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                setImage(bitmap)
                image.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        container.setPeekHeight(height)
                    }
                })
            }
        })

        close_button.setOnClickListener {
            sharingActivity.onBackPressed()
        }
        image_container.setOnClickListener {
            EditImageFragment
                .newInstance(bitmap)
                .show(sharingActivity.supportFragmentManager, "EditImageFragment")
        }
    }

    private fun setImage(bitmap: Bitmap) {
        val height = (image.width * 0.66f).toInt()
        if (bitmap.height > height) {
            image.layoutParams.height = height
        }
        image.setImageBitmap(bitmap)
        image_container.scaleY = 0f
        image_container.scaleX = 0f
        Handler(Looper.getMainLooper()).postDelayed({
            image_container.animate()
                .scaleX(1f)
                .scaleY(1f)
                .duration = 330
        }, 350)
    }

    fun sharePost() {
        val activity = context as SharingContentActivity

        activity.openWaitingFragment()

        val photos = ArrayList<Uri>()
        uri?.let {
            photos.add(it)
        }
        VK.execute(VKWallPostCommand(share_text.text.toString(), photos), object: VKApiCallback<Int> {
            override fun success(result: Int) {
                Toast.makeText(context, context.resources.getString(R.string.photo_posted), Toast.LENGTH_SHORT).show()
                println("WALL OK")
                activity.closeWaitingFragment()
            }

            override fun fail(error: VKApiExecutionException) {
                Toast.makeText(context, context.resources.getString(R.string.photo_failed), Toast.LENGTH_SHORT).show()
                println("WALL ERROR")
            }
        })
    }

    fun close() {
        animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    container.disappear()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .y(AndroidUtils.getScreenHeight(context as SharingContentActivity).toFloat())
            .duration = APPEAR_DURATION
    }

}