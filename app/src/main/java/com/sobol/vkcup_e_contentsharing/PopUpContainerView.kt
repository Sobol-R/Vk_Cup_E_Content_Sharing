package com.sobol.vkcup_e_contentsharing

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.popup_container.view.*

class PopUpContainerView(
    context: Context
) : FrameLayout(context) {

    val APPEAR_DURATION = 150L
    private lateinit var sharingActivity: SharingContentActivity
    private lateinit var behavior: BottomSheetBehavior<ShareContentView>
    private val shareView = ShareContentView(context)
    private val shareButton = ShareButtonView(context)

    fun init(bitmap: Bitmap) {
        LayoutInflater.from(context).inflate(R.layout.popup_container, this, true)
        sharingActivity = context as SharingContentActivity
        shareView.init(this, bitmap)
        shareButton.init()
        initBottomSheetBehavior()
        content.setOnClickListener {
            sharingActivity.onBackPressed()
        }
        content.addView(shareView)
        content.addView(shareButton)
    }

    private fun initBottomSheetBehavior() {
        behavior = BottomSheetBehavior.from(shareView)
        behavior.isHideable = true
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(view: View, state: Int) {
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    sharingActivity.onBackPressed()
                }
            }
        })
    }

    fun setPeekHeight(height: Int) {
        val animator = ValueAnimator.ofInt(0, height)
        animator.addUpdateListener {
            behavior.peekHeight = it.animatedValue as Int
        }
        animator.duration = 300
        animator.start()
    }

    fun appear() {
        sharingActivity.setShareStatusBar()
        content.animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    shareButton.appear()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            .alpha(1f)
            .duration = APPEAR_DURATION
    }

    fun closeShareView() {
        shareView.close()
        closeButton()
    }

    fun closeButton() {
        shareButton.disappear()
    }

    fun disappear() {
        sharingActivity.setUpStatusBar()
        content.animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    sharingActivity.closeShareView()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

            })
            .alpha(0f)
            .duration = APPEAR_DURATION
    }

}