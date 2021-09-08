package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.details

import androidx.lifecycle.*
import com.datasoft.abs.data.dto.transaction.TransactionDetailsResponse
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
class TransactionDetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionDetails = MutableLiveData<Resource<TransactionDetailsResponse>>()
    fun getTransactionDetails(): LiveData<Resource<TransactionDetailsResponse>> = transactionDetails

    init {
        savedStateHandle.get<String>("transaction_no")?.let { transactionNo ->
            transactionDetails(transactionNo)
        }
    }

    private fun transactionDetails(transactionNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            transactionDetails.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getTransactionDetails(transactionNo)
                    transactionDetails.postValue(handleResponse(response))
                } catch (e: Exception) {
                    transactionDetails.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                transactionDetails.postValue(
                    Resource.Error(
                        noInternet, null
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