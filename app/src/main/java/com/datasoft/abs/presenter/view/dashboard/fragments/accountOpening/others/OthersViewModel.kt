package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

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
class OthersViewModel @Inject constructor(

): ViewModel() {

    private val savedPhoto = MutableLiveData<Bitmap>()
    fun getSavedPhoto(): LiveData<Bitmap> = savedPhoto

    private val savedNIDFront = MutableLiveData<Bitmap>()
    fun getSavedNIDFront(): LiveData<Bitmap> = savedNIDFront

    private val savedNIDBack = MutableLiveData<Bitmap>()
    fun getSavedNIDBack(): LiveData<Bitmap> = savedNIDBack

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
}