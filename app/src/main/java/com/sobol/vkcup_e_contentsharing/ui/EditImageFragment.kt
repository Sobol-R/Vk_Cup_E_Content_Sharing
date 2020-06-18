package com.sobol.vkcup_e_contentsharing.ui

import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sobol.vkcup_e_contentsharing.R
import kotlinx.android.synthetic.main.edit_image_fragment.*

private const val BITMAP_KEY = "bitmap"

private const val TOOLBAR_REVEL_DURATION = 300L
private const val IMG_REVEAL_DURATION = 300L

class EditImageFragment : DialogFragment() {

    private var bitmap: Bitmap? = null

    private var toolbarOpen = true

    companion object {
        @JvmStatic
        fun newInstance(bitmap: Bitmap) =
            EditImageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BITMAP_KEY, bitmap)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bitmap = it.getParcelable(BITMAP_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image.setImageBitmap(bitmap)
        image.post { reveal() }

        back.setOnClickListener {
            close()
        }
        view.setOnClickListener {
            if (toolbarOpen)
                hideToolbar()
            else
                openToolbar()
        }
    }

    private fun reveal() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            content.alpha = value
            image.y  = image.height.toFloat() - image.height.toFloat() * value
        }
        animator.duration = IMG_REVEAL_DURATION
        animator.start()
    }

    private fun close() {
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            content.alpha = value
            image.y  = image.height.toFloat() - image.height.toFloat() * value
            if (value == 0f)
                dismiss()
        }
        animator.duration = IMG_REVEAL_DURATION
        animator.start()
    }

    private fun hideToolbar() {
        toolbarOpen = false
        toolbar.animate()
            .alpha(0f)
            .duration = TOOLBAR_REVEL_DURATION
    }

    private fun openToolbar() {
        toolbarOpen = true
        toolbar.animate()
            .alpha(1f)
            .duration = TOOLBAR_REVEL_DURATION
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                close()
            }
        }
    }

}