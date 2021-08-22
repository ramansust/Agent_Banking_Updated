package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.ReceiverDetailsResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
): ViewModel() {

    private val receiverDetails = MutableLiveData<Resource<ReceiverDetailsResponse>>()
    fun getReceiverDetails(): LiveData<Resource<ReceiverDetailsResponse>> = receiverDetails

    fun receiverDetails(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if(accountNo.isEmpty()) {
                receiverDetails.postValue(
                    Resource.Error(
                        "Search filed must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getReceiverDetails(accountNo)
                    receiverDetails.postValue(handleResponse(response))
                } catch (e: Exception) {
                    receiverDetails.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                receiverDetails.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<ReceiverDetailsResponse>): Resource<ReceiverDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}