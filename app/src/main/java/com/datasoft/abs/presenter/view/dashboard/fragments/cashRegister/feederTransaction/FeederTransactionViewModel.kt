package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister.feederTransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerResponse
import com.datasoft.abs.data.dto.transaction.rtgs.CreateRequest
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FeederTransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val createFeeder = MutableLiveData<Event<Resource<CreateCustomerResponse>>>()
    fun getCreationData(): LiveData<Event<Resource<CreateCustomerResponse>>> = createFeeder

    private val feederData = MutableLiveData<Event<Resource<RTGSListResponse>>>()
    fun getFeederData(): LiveData<Event<Resource<RTGSListResponse>>> = feederData

    private val searchData: MutableLiveData<Event<Resource<String>>> = MutableLiveData()
    fun getSearchData(): LiveData<Event<Resource<String>>> = searchData

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Event(Resource.success(search)))
        }
    }

    init {
        requestFeederData(AccountRequest(1, status = "7"))
    }

    private fun requestFeederData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getFeederList(accountRequest)
                    feederData.postValue(handleCustomerResponse(response))
                } catch (e: Exception) {
                    feederData.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                feederData.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    fun createRequest(
        amount: Int,
        bankId: Int,
        charge: Int,
        receiverAccNumber: String,
        receiverAddress: String,
        receiverBranchId: Int,
        receiverCity: String,
        receiverName: String,
        senderAccNumber: String,
        vat: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            createFeeder.postValue(Event(Resource.loading(null)))

            if (receiverAccNumber.isEmpty() || receiverName.isEmpty() || senderAccNumber.isEmpty() || amount == 0) {
                createFeeder.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.createRTGSTransaction(
                        CreateRequest(
                            amount,
                            bankId,
                            charge,
                            "",
                            receiverAccNumber,
                            receiverAddress,
                            receiverBranchId,
                            receiverCity,
                            receiverName,
                            "",
                            "",
                            senderAccNumber,
                            "",
                            "",
                            "",
                            vat
                        )
                    )
                    createFeeder.postValue(handleCreationResponse(response))
                } catch (e: Exception) {
                    createFeeder.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createFeeder.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleCustomerResponse(response: Response<RTGSListResponse>): Event<Resource<RTGSListResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

    private fun handleCreationResponse(response: Response<CreateCustomerResponse>): Event<Resource<CreateCustomerResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

}