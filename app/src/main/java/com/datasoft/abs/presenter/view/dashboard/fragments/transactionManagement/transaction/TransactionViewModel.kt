package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.AccountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AccountDetailsResponse
import com.datasoft.abs.data.dto.transaction.WithdrawDepositRequest
import com.datasoft.abs.data.dto.transaction.WithdrawDepositResponse
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

    private val withdrawDeposit = MutableLiveData<Resource<WithdrawDepositResponse>>()
    fun getWithdrawDeposit(): LiveData<Resource<WithdrawDepositResponse>> = withdrawDeposit

    private val accountNumber = MutableLiveData<String>()
    fun getAccountNumber(): LiveData<String> = accountNumber

    fun accountDetails(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            accountDetails.postValue(Resource.Loading())

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

    fun withdrawDeposit(
        acType: String,
        accountId: Int,
        accountNumber: String,
        charge: Int,
        chargeCode: String,
        chargeInCash: Boolean,
        chargeOnVat: Int,
        credit: Int,
        creditWithoutChargeVat: Int,
        currency2Word: String,
        debit: Int,
        numberOfVerifier: Int,
        remarks: String,
        serviceTypeId: Int,
        taxType: Boolean,
        taxationCode: String,
        transactionType: Int,
        transactionalServiceCode: String,
        trnAmount: Int,
        type: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            withdrawDeposit.postValue(Resource.Loading())

            if (acType.isEmpty() || accountNumber.isEmpty()) {
                withdrawDeposit.postValue(
                    Resource.Error(
                        "Filed must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getWithdrawDeposit(
                        WithdrawDepositRequest(
                            acType,
                            accountId,
                            accountNumber,
                            charge,
                            chargeCode,
                            chargeInCash,
                            chargeOnVat,
                            credit,
                            creditWithoutChargeVat,
                            currency2Word,
                            debit,
                            numberOfVerifier,
                            remarks,
                            serviceTypeId,
                            taxType,
                            taxationCode,
                            transactionType,
                            transactionalServiceCode,
                            trnAmount,
                            type
                        )
                    )
                    withdrawDeposit.postValue(handleWithdrawDepositResponse(response))
                } catch (e: Exception) {
                    withdrawDeposit.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                withdrawDeposit.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    fun setAccountNumber(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            accountNumber.postValue(accountNo)
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

    private fun handleWithdrawDepositResponse(response: Response<WithdrawDepositResponse>): Resource<WithdrawDepositResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}