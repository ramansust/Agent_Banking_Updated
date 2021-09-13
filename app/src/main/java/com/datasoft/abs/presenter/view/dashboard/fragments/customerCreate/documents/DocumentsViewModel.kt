package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.RelatedDoc
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val saveData = MutableLiveData<ArrayList<RelatedDoc>>()
    fun getSavedData(): LiveData<ArrayList<RelatedDoc>> = saveData

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    private val sendMessage = MutableLiveData<Event<Resource<RelatedDoc>>>()
    fun getMessage(): LiveData<Event<Resource<RelatedDoc>>> = sendMessage

    private val documentFrontImage = MutableLiveData<String>()
    fun getDocumentFrontImage(): LiveData<String> = documentFrontImage

    private val documentBackImage = MutableLiveData<String>()
    fun getDocumentBackImage(): LiveData<String> = documentBackImage

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
        description: String,
        frontImage: String,
        backImage: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            sendMessage.postValue(Event(Resource.loading(null)))

            if (documentID.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty() || frontImage.isEmpty()) {
                sendMessage.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
                    )
                )
                return@launch
            }

            val documentInfo = RelatedDoc(
                backImage,
                description,
                name,
                documentType,
                expiryDate,
                frontImage,
                issueDate,
                documentID
            )

            sendMessage.postValue(Event(Resource.success(documentInfo)))
        }
    }

    fun notifyData(documentInfo: RelatedDoc) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<RelatedDoc> = ArrayList()
            list.add(documentInfo)
            saveData.value?.let {
                list.addAll(list.size - 1, it)
            }
            saveData.postValue(list)
        }
    }

    fun removeData(documentInfo: RelatedDoc) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<RelatedDoc> = ArrayList()
            saveData.value?.let {
                list.addAll(it)
            }
            list.remove(documentInfo)
            saveData.postValue(list)
        }
    }

    fun setDocumentFrontImage(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentFrontImage.postValue(value)
        }
    }

    fun setDocumentBackImage(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            documentBackImage.postValue(value)
        }
    }
}