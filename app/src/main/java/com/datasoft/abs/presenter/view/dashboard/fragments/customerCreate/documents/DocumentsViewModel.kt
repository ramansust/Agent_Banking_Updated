package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.RelatedDoc
import com.datasoft.abs.data.dto.createCustomer.toDocument
import com.datasoft.abs.data.source.local.db.CustomerInfo
import com.datasoft.abs.data.source.local.db.entity.customer.Document
import com.datasoft.abs.domain.Repository
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
    private val customerInfo: CustomerInfo,
    private val repository: Repository
) : ViewModel() {

    private var document: LiveData<List<Document>> = repository.getDocuments(customerInfo.customerId)
    fun getDocuments(): LiveData<List<Document>> = document

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
            repository.insertDocument(document)

            sendMessage.postValue(Event(Resource.success(documentInfo)))
        }
    }

    fun removeData(documentID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDocument(documentID)
        }
    }
}