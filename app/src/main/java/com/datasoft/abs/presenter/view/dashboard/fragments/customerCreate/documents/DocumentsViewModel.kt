package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.RelatedDoc
import com.datasoft.abs.data.dto.createCustomer.toDocument
import com.datasoft.abs.data.source.local.db.CustomerInfo
import com.datasoft.abs.data.source.local.db.dao.customer.CustomerDao
import com.datasoft.abs.data.source.local.db.entity.customer.Document
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
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
    private val customerDao: CustomerDao,
    private val customerInfo: CustomerInfo
) : ViewModel() {

    private val document = MutableLiveData<ArrayList<Document>>()
    fun getDocuments(): LiveData<ArrayList<Document>> = document

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    private val sendMessage = MutableLiveData<Event<Resource<RelatedDoc>>>()
    fun getMessage(): LiveData<Event<Resource<RelatedDoc>>> = sendMessage

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

            val document = documentInfo.toDocument()
            document.generalId = customerInfo.customerId
            customerDao.insertDocument(document)

            sendMessage.postValue(Event(Resource.success(documentInfo)))
        }
    }

    fun notifyData(generalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            document.postValue(customerDao.getGeneralWithDocuments(generalId).documents as ArrayList<Document>?)
        }
    }

    fun removeData(documentID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            customerDao.deleteDocument(documentID)
        }
    }
}