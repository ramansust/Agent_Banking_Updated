package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.transaction.DepositResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
): ViewModel() {

    private val balanceData = MutableLiveData<Resource<DepositResponse>>()
    fun getBalanceData(): LiveData<Resource<DepositResponse>> = balanceData

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    fun requestBalanceData(commonRequest: CommonRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getBalanceData(commonRequest)
                    balanceData.postValue(handleResponse(response))
                } catch (e: Exception) {
                    balanceData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                balanceData.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<DepositResponse>): Resource<DepositResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}