package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.transaction.TransactionDetailsResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val transactionDetails = MutableLiveData<Resource<TransactionDetailsResponse>>()
    fun getTransactionDetails(): LiveData<Resource<TransactionDetailsResponse>> = transactionDetails


    fun transactionDetails(transactionNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            transactionDetails.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getTransactionDetails(transactionNo)
                    transactionDetails.postValue(handleResponse(response))
                } catch (e: Exception) {
                    transactionDetails.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                transactionDetails.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<TransactionDetailsResponse>): Resource<TransactionDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}