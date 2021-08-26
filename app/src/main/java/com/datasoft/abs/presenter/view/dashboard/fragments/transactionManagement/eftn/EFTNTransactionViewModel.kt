package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

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
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EFTNTransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
): ViewModel() {

    private val createEFTN = MutableLiveData<Resource<CreateCustomerResponse>>()
    fun getCreationData(): LiveData<Resource<CreateCustomerResponse>> = createEFTN

    private val accountDetails = MutableLiveData<Resource<AccountDetailsResponse>>()
    fun getAccountDetails(): LiveData<Resource<AccountDetailsResponse>> = accountDetails

    private val bankList: MutableLiveData<Resource<List<CommonModel>>> = MutableLiveData()
    fun getBankList(): LiveData<Resource<List<CommonModel>>> = bankList

    private val branchList: MutableLiveData<Resource<List<CommonModel>>> = MutableLiveData()
    fun getBranchList(): LiveData<Resource<List<CommonModel>>> = branchList

    fun requestBankList() {
        viewModelScope.launch(Dispatchers.IO) {

            bankList.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getBankList()
                    bankList.postValue(handleResponse(response))
                } catch (e: Exception) {
                    bankList.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                bankList.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    fun requestBranchList(bankId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            branchList.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getBranchList(bankId)
                    branchList.postValue(handleResponse(response))
                } catch (e: Exception) {
                    branchList.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                branchList.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    fun accountDetails(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            accountDetails.postValue(Resource.Loading())

            if (accountNo.isEmpty()) {
                accountDetails.postValue(
                    Resource.Error(
                        "Search field must not be empty!", null
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
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                accountDetails.postValue(
                    Resource.Error(
                        "No internet connection", null
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

            createEFTN.postValue(Resource.Loading())

            if (receiverAccNumber.isEmpty() || receiverName.isEmpty() || senderAccNumber.isEmpty() || amount == 0) {
                createEFTN.postValue(
                    Resource.Error(
                        "The fields must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.createEFTNTransaction(
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
                    createEFTN.postValue(handleCreationResponse(response))
                } catch (e: Exception) {
                    createEFTN.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createEFTN.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<List<CommonModel>>): Resource<List<CommonModel>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleAccountResponse(response: Response<AccountDetailsResponse>): Resource<AccountDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCreationResponse(response: Response<CreateCustomerResponse>): Resource<CreateCustomerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}