package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.source.local.db.CustomerInfo
import com.datasoft.abs.data.source.local.db.entity.customer.Photo
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: Repository,
    private val customerInfo: CustomerInfo,
) : ViewModel() {

    private val photo: LiveData<Photo> = repository.getPhoto(customerInfo.customerId)
    fun getPhoto(): LiveData<Photo> = photo

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    private val userPhoto = MutableLiveData<String>()
    fun getUserPhoto(): LiveData<String> = userPhoto

    private val documentFront = MutableLiveData<String>()
    fun getDocumentFront(): LiveData<String> = documentFront

    private val documentBack = MutableLiveData<String>()
    fun getDocumentBack(): LiveData<String> = documentBack

    private val signature = MutableLiveData<String>()
    fun getSignature(): LiveData<String> = signature

    private val guardianPhoto = MutableLiveData<String>()
    fun getGuardianPhoto(): LiveData<String> = guardianPhoto

    private val guardianDocumentFront = MutableLiveData<String>()
    fun getGuardianDocumentFront(): LiveData<String> = guardianDocumentFront

    private val guardianDocumentBack = MutableLiveData<String>()
    fun getGuardianDocumentBack(): LiveData<String> = guardianDocumentBack

    private val guardianSignature = MutableLiveData<String>()
    fun getGuardianSignature(): LiveData<String> = guardianSignature

    private val guardianDocumentType = MutableLiveData<Int>()
    fun getGuardianDocumentType(): LiveData<Int> = guardianDocumentType

    fun setBackImage(isRequired: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            backImage.postValue(isRequired)
            guardianDocumentType.postValue(id)
        }
    }

    fun setUserPhoto(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPhoto.postValue(value)
        }
    }

    fun setUserDocumentFront(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentFront.postValue(value)
        }
    }

    fun setUserDocumentBack(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentBack.postValue(value)
        }
    }

    fun setUserSignature(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signature.postValue(value)
        }
    }

    fun setGuardianPhoto(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianPhoto.postValue(value)
        }
    }

    fun setGuardianDocumentFront(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianDocumentFront.postValue(value)
        }
    }

    fun setGuardianDocumentBack(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianDocumentBack.postValue(value)
        }
    }

    fun setGuardianSignature(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            guardianSignature.postValue(value)
        }
    }

    fun insertData() {
        viewModelScope.launch(Dispatchers.IO) {
            val photo = Photo(
                userPhoto.value,
                signature.value,
                documentFront.value,
                documentBack.value,
                guardianPhoto.value,
                guardianSignature.value,
                guardianDocumentFront.value,
                guardianDocumentBack.value,
                guardianDocumentType.value
            )
            photo.generalId = customerInfo.customerId
            repository.insertPhoto(photo)
        }
    }
}