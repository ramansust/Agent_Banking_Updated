package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.signature

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignatureViewModel @Inject constructor(

): ViewModel() {

    private val savedSignature = MutableLiveData<Bitmap>()
    fun getSavedSignature(): LiveData<Bitmap> = savedSignature

    fun setSignature(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            savedSignature.postValue(bitmap)
        }
    }
}