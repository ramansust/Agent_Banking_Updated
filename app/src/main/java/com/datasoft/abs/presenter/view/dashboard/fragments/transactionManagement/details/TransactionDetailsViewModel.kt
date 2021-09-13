package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.details

import androidx.lifecycle.*
import com.datasoft.abs.data.dto.transaction.TransactionDetailsResponse
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
class TransactionDetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionDetails = MutableLiveData<Event<Resource<TransactionDetailsResponse>>>()
    fun getTransactionDetails(): LiveData<Event<Resource<TransactionDetailsResponse>>> =
        transactionDetails

    init {
        savedStateHandle.get<String>("transaction_no")?.let { transactionNo ->
            transactionDetails(transactionNo)
        }
    }

    private fun transactionDetails(transactionNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            transactionDetails.postValue(Event(Resource.loading(null)))

            if (network.isConnected()) {
                try {
                    val response = repository.getTransactionDetails(transactionNo)
                    transactionDetails.postValue(handleResponse(response))
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

    private fun handleResponse(response: Response<TransactionDetailsResponse>): Event<Resource<TransactionDetailsResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }
}