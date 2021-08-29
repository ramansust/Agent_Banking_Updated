package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.RiskGradeResponse
import com.datasoft.abs.data.dto.createCustomer.KYCInfo
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
class KYCViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private val configData = MutableLiveData<Resource<RiskGradeResponse>>()
    fun getConfigData(): LiveData<Resource<RiskGradeResponse>> = configData

    private val kycData = MutableLiveData<KYCInfo>()
    fun getKYCData(): LiveData<KYCInfo> = kycData

    fun configData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getRiskGradeConfigData()
                    configData.postValue(handleConfigResponse(response))
                } catch (e: Exception) {
                    configData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                configData.postValue(
                    Resource.Error(
                        noInternet, null
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
}