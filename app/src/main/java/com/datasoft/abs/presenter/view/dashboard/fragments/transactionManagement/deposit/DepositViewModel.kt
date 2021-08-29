package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.transaction.DepositResponse
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
class DepositViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
): ViewModel() {

    private val depositData = MutableLiveData<Resource<DepositResponse>>()
    fun getDepositData(): LiveData<Resource<DepositResponse>> = depositData

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    init {
        requestDepositData(CommonRequest(1))
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    private fun requestDepositData(commonRequest: CommonRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            depositData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getDepositData(commonRequest)
                    depositData.postValue(handleResponse(response))
                } catch (e: Exception) {
                    depositData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                depositData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<DepositResponse>): Resource<DepositResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}