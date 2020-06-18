package com.sobol.vkcup_e_contentsharing.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sobol.vkcup_e_contentsharing.AndroidUtils
import com.sobol.vkcup_e_contentsharing.R

class ShareButtonView(
    context: Context
) : FrameLayout(context) {

    private lateinit var activity: SharingContentActivity

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.share_button, this, true)

        activity = context as SharingContentActivity

        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM
        layoutParams = params

        alpha = 0f
    }

    fun appear() {
        animate()
            .alpha(1f)
            .duration = 150
    }

    fun disappear() {
        Handler(Looper.getMainLooper()).postDelayed({
            animate()
                .y(AndroidUtils.getScreenHeight(activity).toFloat())
                .duration = 58
        }, 150)
    }

}