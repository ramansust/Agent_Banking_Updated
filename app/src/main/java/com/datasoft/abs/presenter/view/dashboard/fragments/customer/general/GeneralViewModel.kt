package com.datasoft.abs.presenter.view.dashboard.fragments.customer.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.ConfigResponse
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val configData = MutableLiveData<Resource<ConfigResponse>>()
    fun getConfigData(): LiveData<Resource<ConfigResponse>> = configData

    private val dedupeData = MutableLiveData<Resource<DedupeCheckResponse>>()
    fun getDedupeData(): LiveData<Resource<DedupeCheckResponse>> = dedupeData

    fun configData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getConfigData()
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

    fun requestDedupeData(dedupeCheckRequest: DedupeCheckRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getDedupeCheckData(dedupeCheckRequest)
                    dedupeData.postValue(handleDedupeResponse(response))
                } catch (e: Exception) {
                    dedupeData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                dedupeData.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleConfigResponse(response: Response<ConfigResponse>): Resource<ConfigResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleDedupeResponse(response: Response<DedupeCheckResponse>): Resource<DedupeCheckResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}