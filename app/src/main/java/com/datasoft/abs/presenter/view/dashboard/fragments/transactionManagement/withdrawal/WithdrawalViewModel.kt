package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.withdrawal

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
class WithdrawalViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String
) : ViewModel() {

    private var pageNumber = 0

    private val withdrawData = MutableLiveData<Resource<DepositResponse>>()
    fun getWithdrawData(): LiveData<Resource<DepositResponse>> = withdrawData

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
        requestWithdrawData(
            CommonRequest(
                ++pageNumber
            )
        )
    }

    private fun requestWithdrawData(commonRequest: CommonRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            val list = mutableListOf<Row>()
            list.apply {
                withdrawData.value?.data?.rows?.let { addAll(it) }
            }

            withdrawData.postValue(Resource.loading(null))

            if (network.isConnected()) {
                try {

                    val response =
                        handleResponse(repository.getWithdrawData(commonRequest), pageNumber)

                    list.apply {
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    withdrawData.postValue(response)
                } catch (e: Exception) {
                    withdrawData.postValue(
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                withdrawData.postValue(
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