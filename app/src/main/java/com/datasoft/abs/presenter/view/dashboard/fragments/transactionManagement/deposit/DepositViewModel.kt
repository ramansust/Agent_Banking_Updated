package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.transaction.DepositResponse
import com.datasoft.abs.data.dto.transaction.Row
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

    private var pageNumber = 0

    private val depositData = MutableLiveData<Resource<DepositResponse>>()
    fun getDepositData(): LiveData<Resource<DepositResponse>> = depositData

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    init {
        loadMore()
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    fun loadMore() {
        requestDepositData(
            CommonRequest(
                ++pageNumber
            )
        )
    }

    private fun requestDepositData(commonRequest: CommonRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            val list = mutableListOf<Row>()
            list.apply {
                depositData.value?.data?.rows?.let { addAll(it) }
            }

            depositData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = handleResponse(repository.getDepositData(commonRequest), pageNumber)

                    list.apply {
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    depositData.postValue(response)
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

    private fun handleResponse(response: Response<DepositResponse>, pageNumber: Int): Resource<DepositResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.pageNumber = pageNumber
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}