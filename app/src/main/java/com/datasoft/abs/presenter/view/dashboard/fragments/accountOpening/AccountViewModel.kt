package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.AccountConfigResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val configData = MutableLiveData<Resource<AccountConfigResponse>>()
    fun getConfigData(): LiveData<Resource<AccountConfigResponse>> = configData

    private val currentStep: MutableLiveData<Int> = MutableLiveData()
    fun getCurrentStep(): LiveData<Int> = currentStep

    fun accountConfigData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getAccountConfigData()
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

    fun requestCurrentStep(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentStep.postValue(index)
        }
    }

    private fun handleConfigResponse(response: Response<AccountConfigResponse>): Resource<AccountConfigResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
