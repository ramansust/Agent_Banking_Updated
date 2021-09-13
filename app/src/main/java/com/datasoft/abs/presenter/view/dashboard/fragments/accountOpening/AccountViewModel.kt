package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.AccountConfigResponse
import com.datasoft.abs.data.dto.config.TransactionProfileConfig
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
class AccountViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private val addListener: MutableLiveData<Boolean> = MutableLiveData()
    fun getAddListener(): LiveData<Boolean> = addListener

    private val addVisibility: MutableLiveData<Boolean> = MutableLiveData()
    fun getAddVisibility(): LiveData<Boolean> = addVisibility

    private val configData = MutableLiveData<Resource<AccountConfigResponse>>()
    fun getConfigData(): LiveData<Resource<AccountConfigResponse>> = configData

    private val transactionProfile = MutableLiveData<Resource<TransactionProfileConfig>>()
    fun getTransactionProfileData(): LiveData<Resource<TransactionProfileConfig>> =
        transactionProfile

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

    fun transactionProfileData(productID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getTransactionProfileConfigData(productID)
                    transactionProfile.postValue(handleTransactionProfileResponse(response))
                } catch (e: Exception) {
                    transactionProfile.postValue(
                            Resource.error(
                                somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                transactionProfile.postValue(
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

    private fun handleConfigResponse(response: Response<AccountConfigResponse>): Resource<AccountConfigResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }

    private fun handleTransactionProfileResponse(response: Response<TransactionProfileConfig>): Resource<TransactionProfileConfig> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
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
}
