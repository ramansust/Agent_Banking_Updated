package com.datasoft.abs.presenter.view.dashboard.fragments.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.customer.CustomerRequest
import com.datasoft.abs.data.dto.customer.CustomerResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network): ViewModel() {

    private val customerData = MutableLiveData<Resource<CustomerResponse>>()

    fun getCustomerData(): LiveData<Resource<CustomerResponse>> = customerData

    fun requestCustomerData(customerRequest: CustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getCustomerListData(customerRequest)
                    delay(5000)
                    customerData.postValue(handleCustomerResponse(response))
                } catch (e: Exception) {
                    customerData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                customerData.postValue(
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