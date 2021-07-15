package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo

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
class PhotoViewModel @Inject constructor(

): ViewModel() {

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    private val savedPhoto = MutableLiveData<Bitmap>()
    fun getSavedPhoto(): LiveData<Bitmap> = savedPhoto

    private val savedNIDFront = MutableLiveData<Bitmap>()
    fun getSavedNIDFront(): LiveData<Bitmap> = savedNIDFront

    private val savedNIDBack = MutableLiveData<Bitmap>()
    fun getSavedNIDBack(): LiveData<Bitmap> = savedNIDBack

    private val savedSignature = MutableLiveData<Bitmap>()
    fun getSavedSignature(): LiveData<Bitmap> = savedSignature

    fun setPhoto(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            savedPhoto.postValue(bitmap)
        }
    }

    fun setNIDFront(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            savedNIDFront.postValue(bitmap)
        }
    }

    fun setNIDBack(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            savedNIDBack.postValue(bitmap)
        }
    }

    fun setSignature(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            savedSignature.postValue(bitmap)
        }
    }

    fun setBackImage(isRequired: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            backImage.postValue(isRequired)
        }
    }
}