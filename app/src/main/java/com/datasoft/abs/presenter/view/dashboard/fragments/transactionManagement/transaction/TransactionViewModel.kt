package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.AccountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AccountDetailsResponse
import com.datasoft.abs.data.dto.transaction.AmountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AmountDetailsResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val accountDetails = MutableLiveData<Resource<AccountDetailsResponse>>()
    fun getAccountDetails(): LiveData<Resource<AccountDetailsResponse>> = accountDetails

    private val amountDetails = MutableLiveData<Resource<AmountDetailsResponse>>()
    fun getAmountDetails(): LiveData<Resource<AmountDetailsResponse>> = amountDetails

    fun accountDetails(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if (accountNo.isEmpty()) {
                accountDetails.postValue(
                    Resource.Error(
                        "Search filed must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getAccountDetails(AccountDetailsRequest(accountNo))
                    accountDetails.postValue(handleResponse(response))
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

    fun amountDetails(
        acType: String,
        accountNumber: String,
        branchId: Int,
        calculationType: Int,
        trnAmount: Int,
        trnDrOrCr: Int,
        trnProfileType: Int,
        trnType: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            if (trnAmount == 0) {
                amountDetails.postValue(
                    Resource.Error(
                        "Transaction amount must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getAmountDetails(
                        AmountDetailsRequest(
                            acType,
                            accountNumber,
                            branchId,
                            calculationType,
                            trnAmount,
                            trnDrOrCr,
                            trnProfileType,
                            trnType
                        )
                    )
                    amountDetails.postValue(handleAmountResponse(response))
                } catch (e: Exception) {
                    amountDetails.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                amountDetails.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<AccountDetailsResponse>): Resource<AccountDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleAmountResponse(response: Response<AmountDetailsResponse>): Resource<AmountDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}