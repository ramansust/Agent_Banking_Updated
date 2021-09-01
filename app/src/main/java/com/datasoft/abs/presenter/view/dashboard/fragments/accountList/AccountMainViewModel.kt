package com.datasoft.abs.presenter.view.dashboard.fragments.accountList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.accountList.AccountResponse
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AccountMainViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private var activePageNumber = 0
    private var awaitingPageNumber = 0
    private var draftPageNumber = 0

    private val active = MutableLiveData<Resource<AccountResponse>>()
    fun getActiveData(): LiveData<Resource<AccountResponse>> = active

    private val awaiting = MutableLiveData<Resource<AccountResponse>>()
    fun getAwaitingData(): LiveData<Resource<AccountResponse>> = awaiting

    private val draft = MutableLiveData<Resource<AccountResponse>>()
    fun getDraftData(): LiveData<Resource<AccountResponse>> = draft

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    init {
        loadMoreActive()
        loadMoreAwaiting()
        loadMoreDraft()
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    private fun requestActiveData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(repository.getAccountListData(accountRequest), activePageNumber)

                    list.apply {
                        active.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    active.postValue(response)
                } catch (e: Exception) {
                    active.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                active.postValue(
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
                    val response = handleCustomerResponse(repository.getAccountListData(accountRequest), awaitingPageNumber)

                    list.apply {
                        awaiting.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    awaiting.postValue(response)
//                    awaiting.postValue(handleCustomerResponse(response, awaitingPageNumber))
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

    private fun requestDraftData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {

                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(repository.getAccountListData(accountRequest), draftPageNumber)

                    list.apply {
                        draft.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    draft.postValue(response)
//                    draft.postValue(handleCustomerResponse(response, draftPageNumber))
                } catch (e: Exception) {
                    draft.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                draft.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleCustomerResponse(
        response: Response<AccountResponse>,
        pageNumber: Int
    ): Resource<AccountResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.pageNumber = pageNumber
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun loadMoreActive() {
        requestActiveData(
            AccountRequest(
                ++activePageNumber,
                status = "${Status.ACTIVE.type}"
            )
        )
    }

    fun loadMoreAwaiting() {
        requestAwaitingData(
            AccountRequest(
                ++awaitingPageNumber,
                status = "${Status.AWAITING.type}"
            )
        )
    }

    fun loadMoreDraft() {
        requestDraftData(
            AccountRequest(
                ++draftPageNumber,
                status = "${Status.DRAFT.type}"
            )
        )
    }

}