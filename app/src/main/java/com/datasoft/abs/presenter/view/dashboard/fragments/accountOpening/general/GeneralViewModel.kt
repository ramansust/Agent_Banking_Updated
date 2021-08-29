package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.general.AccountInfo
import com.datasoft.abs.data.dto.createAccount.general.CustomerDataResponse
import com.datasoft.abs.data.dto.createAccount.general.DisplayAccountInfo
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
class GeneralViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
    @Named(Constant.ID_EMPTY) private val idEmpty: String,
) : ViewModel() {

    private val customerData = MutableLiveData<Resource<CustomerDataResponse>>()
    fun getCustomerData(): LiveData<Resource<CustomerDataResponse>> = customerData

    private val accountInfo = MutableLiveData<Resource<AccountInfo>>()
    fun getAccountInfo(): LiveData<Resource<AccountInfo>> = accountInfo

    private val displayAccountInfo = MutableLiveData<Resource<DisplayAccountInfo>>()
    fun getDisplayAccountInfo(): LiveData<Resource<DisplayAccountInfo>> = displayAccountInfo

    private val productID = MutableLiveData<Int>()
    fun getProductID(): LiveData<Int> = productID

    fun customerData(customerID: String) {
        viewModelScope.launch(Dispatchers.IO) {

            customerData.postValue(Resource.Loading())

            if (customerID.isEmpty()) {
                customerData.postValue(
                    Resource.Error(
                        idEmpty, null
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
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                customerData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    fun setAccountInfo(
        categoryId: Int,
        categoryValue: String,
        accountId: Int,
        accountValue: String,
        operatingId: Int,
        operatingValue: String,
        customerId: String,
        accountTitle: String,
        openingDate: String,
        currencyId: Int,
        currencyValue: String,
        fundId: Int,
        initialAmount: Int
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            accountInfo.postValue(Resource.Loading())

            if (accountTitle.isEmpty() || openingDate.isEmpty() || initialAmount == 0) {
                accountInfo.postValue(
                    Resource.Error(
                        fieldEmpty, null
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

            displayAccountInfo.postValue(
                Resource.Success(
                    DisplayAccountInfo(
                        categoryValue,
                        accountValue,
                        operatingValue,
                        accountTitle,
                        openingDate,
                        currencyValue,
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