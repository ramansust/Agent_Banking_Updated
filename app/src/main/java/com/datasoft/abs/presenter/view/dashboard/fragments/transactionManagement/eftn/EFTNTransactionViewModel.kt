package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerResponse
import com.datasoft.abs.data.dto.transaction.AccountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AccountDetailsResponse
import com.datasoft.abs.data.dto.transaction.eftn.CreateEFTNRequest
import com.datasoft.abs.data.dto.transaction.rtgs.Details
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
class EFTNTransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
    @Named(Constant.SEARCH_EMPTY) private val searchEmpty: String
) : ViewModel() {

    private val details: MutableLiveData<Int> = MutableLiveData()
    fun getDetailsData(): LiveData<Int> = details

    private val createEFTN = MutableLiveData<Event<Resource<CreateCustomerResponse>>>()
    fun getCreationData(): LiveData<Event<Resource<CreateCustomerResponse>>> = createEFTN

    private val accountDetails = MutableLiveData<Event<Resource<AccountDetailsResponse>>>()
    fun getAccountDetails(): LiveData<Event<Resource<AccountDetailsResponse>>> = accountDetails

    private val bankList: MutableLiveData<Event<Resource<List<CommonModel>>>> = MutableLiveData()
    fun getBankList(): LiveData<Event<Resource<List<CommonModel>>>> = bankList

    private val branchList: MutableLiveData<Event<Resource<List<CommonModel>>>> = MutableLiveData()
    fun getBranchList(): LiveData<Event<Resource<List<CommonModel>>>> = branchList

    private val transactionDetails: MutableLiveData<Event<Resource<Details>>> = MutableLiveData()
    fun getTransactionDetails(): LiveData<Event<Resource<Details>>> = transactionDetails

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
        receiverAccNumber: String,
        receiverBranchId: Int,
        receiverEmail: String,
        receiverMobile: String,
        receiverName: String,
        remarks: String,
        senderAccNumber: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            createEFTN.postValue(Event(Resource.loading(null)))

            if (receiverAccNumber.isEmpty() || receiverName.isEmpty() || senderAccNumber.isEmpty() || amount == 0) {
                createEFTN.postValue(
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
                    val response = repository.createEFTNTransaction(
                        CreateEFTNRequest(
                            amount,
                            bankId,
                            receiverAccNumber,
                            receiverBranchId,
                            receiverEmail,
                            receiverMobile,
                            receiverName,
                            remarks,
                            senderAccNumber
                        )
                    )
                    createEFTN.postValue(handleCreationResponse(response))
                } catch (e: Exception) {
                    createEFTN.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createEFTN.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    fun setDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            details.postValue(id)
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
        return Event(Resource.error(response.message(), null))
    }

    fun detailsData(transactionId: String, isRTGS: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            transactionDetails.postValue(Event(Resource.loading(null)))

            if (network.isConnected()) {
                try {
                    val response =
                        if (isRTGS) repository.getRTGSDetails(transactionId) else repository.getEFTNSDetails(
                            transactionId
                        )
                    transactionDetails.postValue(handleDetailsResponse(response))
                } catch (e: Exception) {
                    transactionDetails.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                transactionDetails.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleDetailsResponse(response: Response<Details>): Event<Resource<Details>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }
}