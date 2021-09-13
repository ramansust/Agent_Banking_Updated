package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountRequest
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountResponse
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
class ReviewViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private val createAccountData = MutableLiveData<Event<Resource<CreateAccountResponse>>>()
    fun getCreateAccountData(): LiveData<Event<Resource<CreateAccountResponse>>> = createAccountData

    private val dataPrepared = MutableLiveData<Boolean>()
    fun getDataPrepared(): LiveData<Boolean> = dataPrepared

    fun createAccount(createAccountRequest: CreateAccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            createAccountData.postValue(Event(Resource.loading(null)))

            if (network.isConnected()) {
                try {
                    val response = repository.createAccountData(createAccountRequest)
                    createAccountData.postValue(handleCreateAccountResponse(response))
                } catch (e: Exception) {
                    createAccountData.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createAccountData.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleCreateAccountResponse(response: Response<CreateAccountResponse>): Event<Resource<CreateAccountResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

    fun setDataPrepared(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataPrepared.postValue(value)
        }
    }
}