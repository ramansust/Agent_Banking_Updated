package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.balance

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
class BalanceViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private var pageNumber = 0

    private val balanceData = MutableLiveData<Resource<DepositResponse>>()
    fun getBalanceData(): LiveData<Resource<DepositResponse>> = balanceData

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    init {
        loadMore()
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.success(search))
        }
    }

    fun loadMore() {
        requestBalanceData(
            CommonRequest(
                ++pageNumber
            )
        )
    }

    private fun requestBalanceData(commonRequest: CommonRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            val list = mutableListOf<Row>()
            list.apply {
                balanceData.value?.data?.rows?.let { addAll(it) }
            }

            balanceData.postValue(Resource.loading(null))

            if (network.isConnected()) {
                try {
                    val response =
                        handleResponse(repository.getBalanceData(commonRequest), pageNumber)

                    list.apply {
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    balanceData.postValue(response)
                } catch (e: Exception) {
                    balanceData.postValue(
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                balanceData.postValue(
                    Resource.error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleResponse(
        response: Response<DepositResponse>,
        pageNumber: Int
    ): Resource<DepositResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.pageNumber = pageNumber
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }
}