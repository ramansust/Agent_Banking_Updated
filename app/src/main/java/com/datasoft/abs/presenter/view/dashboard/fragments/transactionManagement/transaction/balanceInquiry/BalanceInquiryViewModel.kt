package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.BalanceInquiryResponse
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
class BalanceInquiryViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.SEARCH_EMPTY) private val searchEmpty: String
) : ViewModel() {

    private val balanceInquiry = MutableLiveData<Resource<BalanceInquiryResponse>>()
    fun getBalanceInquiry(): LiveData<Resource<BalanceInquiryResponse>> = balanceInquiry

    fun balanceInquiry(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            balanceInquiry.postValue(Resource.Loading())

            if (accountNo.isEmpty()) {
                balanceInquiry.postValue(
                    Resource.Error(
                        searchEmpty, null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getBalanceInquiry(accountNo)
                    balanceInquiry.postValue(handleResponse(response))
                } catch (e: Exception) {
                    balanceInquiry.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                balanceInquiry.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<BalanceInquiryResponse>): Resource<BalanceInquiryResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}