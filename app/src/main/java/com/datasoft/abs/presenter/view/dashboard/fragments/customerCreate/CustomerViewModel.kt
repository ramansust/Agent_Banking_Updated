package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.ConfigResponse
import com.datasoft.abs.data.dto.createCustomer.DocumentVerificationInfo
import com.datasoft.abs.data.dto.createCustomer.toDocumentIdentification
import com.datasoft.abs.data.source.local.db.dao.customer.CustomerDao
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    private val customerDao: CustomerDao,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private val configData = MutableLiveData<Resource<ConfigResponse>>()
    fun getConfigData(): LiveData<Resource<ConfigResponse>> = configData

    private val currentStep: MutableLiveData<Int> = MutableLiveData()
    fun getCurrentStep(): LiveData<Int> = currentStep

    private val addListener: MutableLiveData<Boolean> = MutableLiveData()
    fun getAddListener(): LiveData<Boolean> = addListener

    private val addVisibility: MutableLiveData<Boolean> = MutableLiveData()
    fun getAddVisibility(): LiveData<Boolean> = addVisibility

    private val documentList: MutableLiveData<List<DocumentVerificationInfo>> = MutableLiveData()
    fun getDocumentList(): LiveData<List<DocumentVerificationInfo>> = documentList

    fun configData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getConfigData()
                    configData.postValue(handleConfigResponse(response))
                } catch (e: Exception) {
                    configData.postValue(
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                configData.postValue(
                    Resource.error(
                        noInternet, null
                    )
                )
            }
        }
    }

    fun requestCurrentStep(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentStep.postValue(index)
        }
    }

    fun requestListener(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            addListener.postValue(value)
        }
    }

    fun requestVisibility(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            addVisibility.postValue(value)
        }
    }

    private fun handleConfigResponse(response: Response<ConfigResponse>): Resource<ConfigResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }

    fun setDocumentList() {
        val documents = mutableListOf<DocumentVerificationInfo>()

        documents.add(
            DocumentVerificationInfo(
                "NID",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Passport",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Birth Certificate",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "e-Tin",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Driving License",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Vat Registration",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Organization Registration",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.add(
            DocumentVerificationInfo(
                "Certificate of Incorporation",
                isPhotocopyCollected = false,
                isVerified = false
            )
        )

        documents.forEach {
            val documentIdentification = it.toDocumentIdentification()
            documentIdentification.generalId = 1
            viewModelScope.launch(Dispatchers.IO) {
                customerDao.insertDocumentIdentification(documentIdentification)
            }
        }

        documentList.postValue(documents)
    }

    fun collectedDocument(index: Int, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            documentList.value?.get(index)?.isPhotocopyCollected = isChecked
            documentList.postValue(documentList.value)
        }
    }

    fun verifiedDocument(index: Int, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            documentList.value?.get(index)?.isVerified = isChecked
            documentList.postValue(documentList.value)
        }
    }

}
