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

    private val userPhoto = MutableLiveData<Bitmap>()
    fun getUserPhoto(): LiveData<Bitmap> = userPhoto

    private val userDocumentFront = MutableLiveData<Bitmap>()
    fun getUserDocumentFront(): LiveData<Bitmap> = userDocumentFront

    private val userDocumentBack = MutableLiveData<Bitmap>()
    fun getUserDocumentBack(): LiveData<Bitmap> = userDocumentBack

    private val userSignature = MutableLiveData<Bitmap>()
    fun getUserSignature(): LiveData<Bitmap> = userSignature

    private val guardianPhoto = MutableLiveData<Bitmap>()
    fun getGuardianPhoto(): LiveData<Bitmap> = guardianPhoto

    private val guardianDocumentFront = MutableLiveData<Bitmap>()
    fun getGuardianDocumentFront(): LiveData<Bitmap> = guardianDocumentFront

    private val guardianDocumentBack = MutableLiveData<Bitmap>()
    fun getGuardianDocumentBack(): LiveData<Bitmap> = guardianDocumentBack

    private val guardianSignature = MutableLiveData<Bitmap>()
    fun getGuardianSignature(): LiveData<Bitmap> = guardianSignature

    private val guardianDocumentType = MutableLiveData<Int>()
    fun getGuardianDocumentType(): LiveData<Int> = guardianDocumentType

    fun setUserPhoto(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userPhoto.postValue(bitmap)
        }
    }

    fun setUserDocumentFront(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userDocumentFront.postValue(bitmap)
        }
    }

    fun setUserDocumentBack(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userDocumentBack.postValue(bitmap)
        }
    }

    fun setUserSignature(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            userSignature.postValue(bitmap)
        }
    }

    fun setBackImage(isRequired: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            backImage.postValue(isRequired)
            guardianDocumentType.postValue(id)
        }
    }

    fun setGuardianPhoto(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianPhoto.postValue(bitmap)
        }
    }

    fun setGuardianDocumentFront(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianDocumentFront.postValue(bitmap)
        }
    }

    fun setGuardianDocumentBack(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianDocumentBack.postValue(bitmap)
        }
    }

    fun setGuardianSignature(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianSignature.postValue(bitmap)
        }
    }
}