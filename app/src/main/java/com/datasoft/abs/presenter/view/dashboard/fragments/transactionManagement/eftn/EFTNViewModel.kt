package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.NO_INTERNET
import com.datasoft.abs.presenter.utils.Constant.SOMETHING_WRONG
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.utils.RTGSStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EFTNViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(NO_INTERNET) private val noInternet: String,
    @Named(SOMETHING_WRONG) private val somethingWrong: String
) : ViewModel() {

    private var awaitingPageNumber = 0
    private var disbursePageNumber = 0
    private var rejectPageNumber = 0

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    private val awaiting = MutableLiveData<Resource<RTGSListResponse>>()
    fun getAwaitingData(): LiveData<Resource<RTGSListResponse>> = awaiting

    private val disburse = MutableLiveData<Resource<RTGSListResponse>>()
    fun getDisburseData(): LiveData<Resource<RTGSListResponse>> = disburse

    private val reject = MutableLiveData<Resource<RTGSListResponse>>()
    fun getRejectData(): LiveData<Resource<RTGSListResponse>> = reject

    init {
        loadMoreAwaiting()
        loadMoreDisburse()
        loadMoreReject()
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    private fun requestDisburseData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(
                        repository.getEFTNList(accountRequest),
                        disbursePageNumber
                    )

                    list.apply {
                        disburse.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    disburse.postValue(response)
                } catch (e: Exception) {
                    disburse.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                disburse.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun requestAwaitingData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(
                        repository.getEFTNList(accountRequest),
                        awaitingPageNumber
                    )

                    list.apply {
                        awaiting.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    awaiting.postValue(response)
                } catch (e: Exception) {
                    awaiting.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                awaiting.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun requestRejectData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {

                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(
                        repository.getEFTNList(accountRequest),
                        rejectPageNumber
                    )

                    list.apply {
                        reject.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    reject.postValue(response)
                } catch (e: Exception) {
                    reject.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                reject.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleCustomerResponse(
        response: Response<RTGSListResponse>,
        pageNumber: Int
    ): Resource<RTGSListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.pageNumber = pageNumber
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun loadMoreDisburse() {
        requestDisburseData(
            AccountRequest(
                ++disbursePageNumber,
                status = "${RTGSStatus.DISBURSE.type}"
            )
        )
    }

    fun loadMoreAwaiting() {
        requestAwaitingData(
            AccountRequest(
                ++awaitingPageNumber,
                status = "${RTGSStatus.AWAITING.type}"
            )
        )
    }

    fun loadMoreReject() {
        requestRejectData(
            AccountRequest(
                ++rejectPageNumber,
                status = "${RTGSStatus.REJECT.type}"
            )
        )
    }
}