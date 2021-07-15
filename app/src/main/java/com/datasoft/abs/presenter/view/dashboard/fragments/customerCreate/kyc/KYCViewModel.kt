package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.RiskGradeResponse
import com.datasoft.abs.data.dto.createCustomer.DocumentVerificationInfo
import com.datasoft.abs.data.dto.createCustomer.KYCInfo
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class KYCViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val configData = MutableLiveData<Resource<RiskGradeResponse>>()
    fun getConfigData(): LiveData<Resource<RiskGradeResponse>> = configData

    private val kycData = MutableLiveData<KYCInfo>()
    fun getKYCData(): LiveData<KYCInfo> = kycData

    private val documentList = MutableLiveData<ArrayList<DocumentVerificationInfo>>()
    fun getDocumentList(): LiveData<ArrayList<DocumentVerificationInfo>> = documentList

    fun configData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getRiskGradeConfigData()
                    configData.postValue(handleConfigResponse(response))
                } catch (e: Exception) {
                    configData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                configData.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    fun saveData(
        typeOfOnboarding: Int,
        residentStatus: Int,
        blackListed: Int,
        isPep: Int,
        isPepCloser: Int,
        isInterviewedPersonally: Int,
        typeOfProduct: Int,
        profession: Int,
        transactionalRisk: Int,
        transparencyRisk: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val kycInfo = KYCInfo(
                typeOfOnboarding,
                residentStatus,
                blackListed,
                isPep,
                isPepCloser,
                isInterviewedPersonally,
                typeOfProduct,
                profession,
                transactionalRisk,
                transparencyRisk
            )
            kycData.postValue(kycInfo)
        }
    }

    private fun handleConfigResponse(response: Response<RiskGradeResponse>): Resource<RiskGradeResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun documentList(list: ArrayList<DocumentVerificationInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (documentList.value == null) {
                documentList.postValue(list)
            }
        }
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