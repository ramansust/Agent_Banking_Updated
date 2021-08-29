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

    private val createAccountData = MutableLiveData<Resource<CreateAccountResponse>>()
    fun getCreateAccountData(): LiveData<Resource<CreateAccountResponse>> = createAccountData

    private val dataPrepared = MutableLiveData<Boolean>()
    fun getDataPrepared(): LiveData<Boolean> = dataPrepared

    fun createAccount(createAccountRequest: CreateAccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            createAccountData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.createAccountData(createAccountRequest)
                    createAccountData.postValue(handleCreateAccountResponse(response))
                } catch (e: Exception) {
                    createAccountData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createAccountData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleCreateAccountResponse(response: Response<CreateAccountResponse>): Resource<CreateAccountResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun setDataPrepared(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataPrepared.postValue(value)
        }
    }
}