package com.datasoft.abs.presenter.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.setPadding

class ProgressDialog(context: Context) : Dialog(context) {

    private val message: TextView = TextView(context)
    private val progressBar = ProgressBar(context)
    private var container = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }

    init {
        val paddingInDp = 8
        val scale = context.resources.displayMetrics.density
        val paddingInPx = (paddingInDp * scale + 0.5f).toInt()

        message.text = "Loading. Please wait ..."

        container.addView(progressBar)
        container.addView(message)
        container.setPadding(paddingInPx)

        setContentView(container)
        this.setOnDismissListener { }
    }
}