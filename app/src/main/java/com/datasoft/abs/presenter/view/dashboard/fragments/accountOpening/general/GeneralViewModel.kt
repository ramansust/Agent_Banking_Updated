package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.general.*
import com.datasoft.abs.data.source.local.db.entity.account.Account
import com.datasoft.abs.data.source.local.db.entity.account.Customer
import com.datasoft.abs.data.source.local.db.entity.account.toAccountInfo
import com.datasoft.abs.data.source.local.db.entity.account.toCustomerData
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private val accountInfo = MutableLiveData<Event<Resource<AccountInfo>>>()
    fun getAccountInfo(): LiveData<Event<Resource<AccountInfo>>> = accountInfo

    private val displayAccountInfo = MutableLiveData<Resource<DisplayAccountInfo>>()
    fun getDisplayAccountInfo(): LiveData<Resource<DisplayAccountInfo>> = displayAccountInfo

    private val productID = MutableLiveData<Int>()
    fun getProductID(): LiveData<Int> = productID

    fun customerData(customerID: String) {
        viewModelScope.launch(Dispatchers.IO) {

            customerData.postValue(Resource.loading(null))

            if (customerID.isEmpty()) {
                customerData.postValue(
                    Resource.error(
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
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                customerData.postValue(
                    Resource.error(
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

            accountInfo.postValue(Event(Resource.loading(null)))

            if (accountTitle.isEmpty() || openingDate.isEmpty() || initialAmount == 0) {
                accountInfo.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
                    )
                )
                return@launch
            }

            val account = Account(
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

            val id = repository.insertAccount(account)
            repository.setAccountId(id.toInt())

            delay(1000)

            val list = mutableListOf<Customer>()
            customerData.value!!.data.let {
                it!!.customerData.map { customerData ->
                    val customer = customerData.toCustomer()
                    customer.accountId = id.toInt()
                    list.add(customer)
                }
            }

            repository.insertCustomers(list)

            accountInfo.postValue(
                Event(
                    Resource.success(
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
            )

            displayAccountInfo.postValue(
                Resource.success(
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
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }

    fun setProductID(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productID.postValue(value)
        }
    }

    fun showData() {
        viewModelScope.launch(Dispatchers.IO) {
            accountInfo.postValue(
                Event(
                    Resource.success(
                        repository.getAccountWithCustomers(1).account.toAccountInfo()
                    )
                )
            )

            val list = mutableListOf<CustomerData>()

            val customers = repository.getAccountWithCustomers(1).customers
            customers.forEach {
                list.add(it.toCustomerData())
            }

//            customerData.postValue(Resource.success(list))
        }
    }

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(1)
        }
    }

    fun insertCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Customer>()
            customerData.value!!.data.let {
                it!!.customerData.map { customerData ->
                    val customer = customerData.toCustomer()
                    customer.accountId = 10
                    list.add(customer)
                }
            }

            repository.insertCustomers(list)
        }
    }
}