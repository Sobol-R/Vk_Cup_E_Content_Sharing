package com.sobol.vkcup_e_contentsharing.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sobol.vkcup_e_contentsharing.R

class WaitingFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.waiting_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            if (context != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!,
                    R.color.colorTransparentBackground
                )))
        }
    }

}