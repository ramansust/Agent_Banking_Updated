package com.datasoft.abs

import android.app.Application
import com.gu.toolargetool.TooLargeTool
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TooLargeTool.startLogging(this)
    }
}