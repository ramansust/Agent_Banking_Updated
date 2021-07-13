package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.DocumentInfo
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(

): ViewModel() {

    private val saveData = MutableLiveData<ArrayList<DocumentInfo>>()
    fun getSavedData(): LiveData<ArrayList<DocumentInfo>> = saveData

    private val savedSignature = MutableLiveData<Bitmap>()
    fun getSavedSignature(): LiveData<Bitmap> = savedSignature

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    private val sendMessage = MutableLiveData<Resource<DocumentInfo>>()
    fun getMessage(): LiveData<Resource<DocumentInfo>> = sendMessage

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

    fun checkData(
        documentType: Int,
        name: String,
        issueDate: String,
        documentID: String,
        expiryDate: String,
        description: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            sendMessage.postValue(Resource.Loading())

            if (documentID.isEmpty() || issueDate.isEmpty()) {
                sendMessage.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            val documentInfo = DocumentInfo(
                documentType, name, documentID, issueDate, expiryDate, description
            )

            sendMessage.postValue(Resource.Success(documentInfo))
        }
    }

    fun notifyData(documentInfo: DocumentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<DocumentInfo> = ArrayList()
            list.add(documentInfo)
            saveData.value?.let {
                list.addAll(list.size - 1, it)
            }
            saveData.postValue(list)
        }
    }

    fun removeData(documentInfo: DocumentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<DocumentInfo> = ArrayList()
            saveData.value?.let {
                list.addAll(it)
            }
            list.remove(documentInfo)
            saveData.postValue(list)
        }
    }
}