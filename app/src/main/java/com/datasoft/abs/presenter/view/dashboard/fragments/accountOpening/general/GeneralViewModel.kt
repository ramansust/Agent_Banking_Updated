package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.general.AccountInfo
import com.datasoft.abs.data.dto.createAccount.general.CustomerDataResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
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

    private val customerData = MutableLiveData<Resource<CustomerDataResponse>>()
    fun getCustomerData(): LiveData<Resource<CustomerDataResponse>> = customerData

    private val accountInfo = MutableLiveData<Resource<AccountInfo>>()
    fun getAccountInfo(): LiveData<Resource<AccountInfo>> = accountInfo

    private val productID = MutableLiveData<Int>()
    fun getProductID(): LiveData<Int> = productID

    fun customerData(customerID: String) {
        viewModelScope.launch(Dispatchers.IO) {

            customerData.postValue(Resource.Loading())

            if (customerID.isEmpty()) {
                customerData.postValue(
                    Resource.Error(
                        "Customer ID must not be empty", null
                    )
                )
                return@launch
            }

            if (network.isConnected()) {

                try {
                    val response = repository.getCustomerData(customerID)
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

    fun setAccountInfo(
        categoryId: Int,
        accountId: Int,
        operatingId: Int,
        customerId: String,
        accountTitle: String,
        openingDate: String,
        currencyId: Int,
        fundId: Int,
        initialAmount: Int
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            accountInfo.postValue(Resource.Loading())

            if (accountTitle.isEmpty() || openingDate.isEmpty() || initialAmount == 0) {
                accountInfo.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            accountInfo.postValue(
                Resource.Success(
                    AccountInfo(
                        categoryId,
                        accountId,
                        operatingId,
                        customerId,
                        accountTitle,
                        openingDate,
                        currencyId,
                        fundId,
                        initialAmount
                    )
                )
            )
        }
    }

    private fun handleCustomerResponse(
        response: Response<CustomerDataResponse>
    ): Resource<CustomerDataResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun setProductID(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productID.postValue(value)
        }
    }
}