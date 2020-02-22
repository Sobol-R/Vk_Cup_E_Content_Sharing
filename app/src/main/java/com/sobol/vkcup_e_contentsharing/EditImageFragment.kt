package com.sobol.vkcup_e_contentsharing

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.edit_image_fragment.*


@SuppressLint("ValidFragment")
class EditImageFragment(
    private val bitmap: Bitmap
) : DialogFragment() {

    private var toolbarOpen = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image.setImageBitmap(bitmap)
        back.setOnClickListener {
            dismiss()
        }
        view.setOnClickListener {
            if (toolbarOpen)
                hideToolbar()
            else
                openToolbar()
        }
    }

    private fun hideToolbar() {
        toolbarOpen = false
        toolbar.animate()
            .y(-toolbar.height.toFloat())
            .duration = 250
    }

    private fun openToolbar() {
        toolbarOpen = true
        toolbar.animate()
            .y(0f)
            .duration = 250
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        }
    }

}