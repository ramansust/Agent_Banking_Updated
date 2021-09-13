package com.datasoft.abs.presenter.utils

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class ToastHelper @Inject constructor() {

    private val toastEmitter: EventEmitter<Event<String>> = EventEmitter()
    val toastMessages: EventSource<Event<String>> = toastEmitter

    fun sendToast(message: String) {
        toastEmitter.emit(Event(message))
    }
}