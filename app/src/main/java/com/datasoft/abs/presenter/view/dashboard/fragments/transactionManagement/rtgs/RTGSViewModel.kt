package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.utils.RTGSStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RTGSViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String
) : ViewModel() {

    private var awaitingPageNumber = 0
    private var disbursePageNumber = 0
    private var rejectPageNumber = 0

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

    private fun requestDisburseData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(
                        repository.getRTGSList(accountRequest),
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
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                disburse.postValue(
                    Resource.error(
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
                        repository.getRTGSList(accountRequest),
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
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                awaiting.postValue(
                    Resource.error(
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
                        repository.getRTGSList(accountRequest),
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
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                reject.postValue(
                    Resource.error(
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
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
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