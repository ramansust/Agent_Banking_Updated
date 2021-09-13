package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerResponse
import com.datasoft.abs.data.dto.transaction.AccountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AccountDetailsResponse
import com.datasoft.abs.data.dto.transaction.rtgs.CreateRequest
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
class RTGSTransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
    @Named(Constant.SEARCH_EMPTY) private val searchEmpty: String
) : ViewModel() {

    private val createRTGS = MutableLiveData<Event<Resource<CreateCustomerResponse>>>()
    fun getCreationData(): LiveData<Event<Resource<CreateCustomerResponse>>> = createRTGS

    private val accountDetails = MutableLiveData<Event<Resource<AccountDetailsResponse>>>()
    fun getAccountDetails(): LiveData<Event<Resource<AccountDetailsResponse>>> = accountDetails

    private val bankList: MutableLiveData<Event<Resource<List<CommonModel>>>> = MutableLiveData()
    fun getBankList(): LiveData<Event<Resource<List<CommonModel>>>> = bankList

    private val branchList: MutableLiveData<Event<Resource<List<CommonModel>>>> = MutableLiveData()
    fun getBranchList(): LiveData<Event<Resource<List<CommonModel>>>> = branchList

    init {
        requestBankList()
    }

    private fun requestBankList() {
        viewModelScope.launch(Dispatchers.IO) {

            bankList.postValue(Event(Resource.loading(null)))

            if (network.isConnected()) {
                try {
                    val response = repository.getBankList()
                    bankList.postValue(handleResponse(response))
                } catch (e: Exception) {
                    bankList.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                bankList.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    fun requestBranchList(bankId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            branchList.postValue(Event(Resource.loading(null)))

            if (network.isConnected()) {
                try {
                    val response = repository.getBranchList(bankId)
                    branchList.postValue(handleResponse(response))
                } catch (e: Exception) {
                    branchList.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                branchList.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    fun accountDetails(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            accountDetails.postValue(Event(Resource.loading(null)))

            if (accountNo.isEmpty()) {
                accountDetails.postValue(
                    Event(
                        Resource.error(
                            searchEmpty, null
                        )
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getAccountDetails(AccountDetailsRequest(accountNo))
                    accountDetails.postValue(handleAccountResponse(response))
                } catch (e: Exception) {
                    accountDetails.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                accountDetails.postValue(
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

            createRTGS.postValue(Event(Resource.loading(null)))

            if (receiverAccNumber.isEmpty() || receiverName.isEmpty() || senderAccNumber.isEmpty() || amount == 0) {
                createRTGS.postValue(
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
                    createRTGS.postValue(handleCreationResponse(response))
                } catch (e: Exception) {
                    createRTGS.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createRTGS.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<List<CommonModel>>): Event<Resource<List<CommonModel>>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

    private fun handleAccountResponse(response: Response<AccountDetailsResponse>): Event<Resource<AccountDetailsResponse>> {
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
        return Event(Resource.error(response.errorBody()!!.string(), null))
    }
}