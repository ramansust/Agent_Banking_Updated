package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerRequest
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerResponse
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

    private val createCustomerData = MutableLiveData<Resource<CreateCustomerResponse>>()
    fun getCreateCustomerData(): LiveData<Resource<CreateCustomerResponse>> = createCustomerData

    fun createCustomer(createCustomerRequest: CreateCustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            createCustomerData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.createCustomerData(createCustomerRequest)
                    createCustomerData.postValue(handleCreateCustomerResponse(response))
                } catch (e: Exception) {
                    createCustomerData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                createCustomerData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleCreateCustomerResponse(response: Response<CreateCustomerResponse>): Resource<CreateCustomerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}