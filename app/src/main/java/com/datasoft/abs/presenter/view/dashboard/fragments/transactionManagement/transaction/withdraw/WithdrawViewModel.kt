package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.withdraw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.AmountDetailsRequest
import com.datasoft.abs.data.dto.transaction.AmountDetailsResponse
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
class WithdrawViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String
) : ViewModel() {

    private val amountDetails = MutableLiveData<Resource<AmountDetailsResponse>>()
    fun getAmountDetails(): LiveData<Resource<AmountDetailsResponse>> = amountDetails

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
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                amountDetails.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleAmountResponse(response: Response<AmountDetailsResponse>): Resource<AmountDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.errorBody()!!.string())
    }
}