package com.datasoft.abs.presenter.view.dashboard.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.customer.CustomerRequest
import com.datasoft.abs.data.dto.customer.CustomerResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountMainViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val accountData = MutableLiveData<Resource<CustomerResponse>>()
    fun getAllAccountData(): LiveData<Resource<CustomerResponse>> = accountData

    private val searchData: MutableLiveData<String> = MutableLiveData()
    fun getSearchData(): LiveData<String> = searchData

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(search)
        }
    }

    fun requestCustomerData(customerRequest: CustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getCustomerListData(customerRequest)
                    accountData.postValue(handleCustomerResponse(response))
                } catch (e: Exception) {
                    accountData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                accountData.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleCustomerResponse(response: Response<CustomerResponse>): Resource<CustomerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}