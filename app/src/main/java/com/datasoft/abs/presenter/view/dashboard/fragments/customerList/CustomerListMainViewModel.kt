package com.datasoft.abs.presenter.view.dashboard.fragments.customerList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.customerList.CustomerRequest
import com.datasoft.abs.data.dto.customerList.CustomerResponse
import com.datasoft.abs.data.dto.customerList.Row
import com.datasoft.abs.data.source.local.db.entity.customer.General
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
class CustomerListMainViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private var activePageNumber = 0
    private var awaitingPageNumber = 0
    private var draftPageNumber = 0

    private val active = MutableLiveData<Resource<CustomerResponse>>()
    fun getActiveData(): LiveData<Resource<CustomerResponse>> = active

    private val awaiting = MutableLiveData<Resource<CustomerResponse>>()
    fun getAwaitingData(): LiveData<Resource<CustomerResponse>> = awaiting

    private val draft: LiveData<List<General>> = repository.getAllCustomer()
    fun getDraftData(): LiveData<List<General>> = draft

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    init {
        loadMoreActive()
        loadMoreAwaiting()
//        loadMoreDraft()
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.success(search))
        }
    }

    private fun requestActiveData(customerRequest: CustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(repository.getCustomerListData(customerRequest), activePageNumber)

                    list.apply {
                        active.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    active.postValue(response)
                } catch (e: Exception) {
                    active.postValue(
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                active.postValue(
                    Resource.error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun requestAwaitingData(customerRequest: CustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(repository.getCustomerListData(customerRequest), awaitingPageNumber)

                    list.apply {
                        awaiting.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    awaiting.postValue(response)
//                    awaiting.postValue(handleCustomerResponse(response, awaitingPageNumber))
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

    /*private fun requestDraftData(customerRequest: CustomerRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val list = mutableListOf<Row>()
                    val response = handleCustomerResponse(repository.getCustomerListData(customerRequest), draftPageNumber)

                    list.apply {
                        draft.value?.data?.rows?.let { addAll(it) }
                        addAll(response.data?.rows!!)
                    }

                    response.data!!.rows = list
                    draft.postValue(response)
//                    draft.postValue(handleCustomerResponse(response, draftPageNumber))
                } catch (e: Exception) {
                    draft.postValue(
                        Resource.error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                draft.postValue(
                    Resource.error(
                        noInternet, null
                    )
                )
            }
        }
    }*/

    private fun handleCustomerResponse(response: Response<CustomerResponse>, pageNumber: Int): Resource<CustomerResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.pageNumber = pageNumber
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }

    fun loadMoreActive() {
        requestActiveData(
            CustomerRequest(
                ++activePageNumber,
                status = "${Status.ACTIVE.type}"
            )
        )
    }

    fun loadMoreAwaiting() {
        requestAwaitingData(
            CustomerRequest(
                ++awaitingPageNumber,
                status = "${Status.AWAITING.type}"
            )
        )
    }

    /*fun loadMoreDraft() {
        requestDraftData(
            CustomerRequest(
                ++draftPageNumber,
                status = "${Status.DRAFT.type}"
            )
        )
    }*/

}