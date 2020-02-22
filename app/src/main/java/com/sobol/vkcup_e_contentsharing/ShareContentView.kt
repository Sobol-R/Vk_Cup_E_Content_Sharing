package com.sobol.vkcup_e_contentsharing

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.share_content_view.view.*

class ShareContentView(
    context: Context
) : FrameLayout(context) {

    val APPEAR_DURATION = 250L
    private lateinit var container: PopUpContainerView


    fun init(container: PopUpContainerView, bitmap: Bitmap) {
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
            val sharingActivity = context as SharingContentActivity
            sharingActivity.onBackPressed()
        }
        image_container.setOnClickListener {
            val activity = context as SharingContentActivity
            EditImageFragment(bitmap).show(activity.supportFragmentManager, "EditImageFragment")
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