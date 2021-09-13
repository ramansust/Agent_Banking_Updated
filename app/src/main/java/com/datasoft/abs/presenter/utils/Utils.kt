package com.datasoft.abs.presenter.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import android.widget.FrameLayout
import android.view.ViewGroup

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    val snackBarView = Snackbar.make(this, message, duration)
    val view = snackBarView.view
    val params: ViewGroup.LayoutParams = snackBarView.view.layoutParams
    if (params is CoordinatorLayout.LayoutParams) {
        params.gravity = Gravity.TOP
    } else {
        (params as FrameLayout.LayoutParams).gravity = Gravity.TOP
    }
    view.layoutParams = params
//    view.background = ContextCompat.getDrawable(context, R.drawable.input_border) // for custom background
    snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBarView.show()
}
