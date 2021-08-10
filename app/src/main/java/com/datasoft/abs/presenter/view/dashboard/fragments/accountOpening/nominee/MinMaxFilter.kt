package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.text.InputFilter
import android.text.Spanned


class MinMaxFilter() : InputFilter {
    private var intMin: Int = 0
    private var intMax: Int = 0

    constructor(minValue: String, maxValue: String) : this() {
        this.intMin = Integer.parseInt(minValue)
        this.intMax = Integer.parseInt(maxValue)
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            // Remove the string out of destination that is to be replaced
            var newVal = dest.toString().substring(0, dstart) + dest.toString()
                .substring(dend, dest.toString().length)

            // Add the new string in
            newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(
                dstart, newVal.length
            )
            val input = newVal.toInt()
            if (isInRange(intMin, intMax, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}