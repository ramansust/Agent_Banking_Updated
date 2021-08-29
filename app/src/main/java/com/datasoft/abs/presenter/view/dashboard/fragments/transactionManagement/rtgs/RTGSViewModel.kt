package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.transaction.rtgs.Details
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
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
class RTGSViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String
) : ViewModel() {

    private val details: MutableLiveData<Int> = MutableLiveData()
    fun getDetailsData(): LiveData<Int> = details

    private val transactionDetails: MutableLiveData<Resource<Details>> = MutableLiveData()
    fun getTransactionDetails(): LiveData<Resource<Details>> = transactionDetails

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    private val rtgsData = MutableLiveData<Resource<RTGSListResponse>>()
    fun getRTGSData(): LiveData<Resource<RTGSListResponse>> = rtgsData

    init {
        requestRTGSData(AccountRequest(1, status = "7"))
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    fun setDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            details.postValue(id)
        }
    }

    private fun requestRTGSData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            rtgsData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getRTGSList(accountRequest)
                    rtgsData.postValue(handleResponse(response))
                } catch (e: Exception) {
                    rtgsData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                rtgsData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    fun detailsData(transactionId: String, isRTGS: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {

            transactionDetails.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response =
                        if (isRTGS) repository.getRTGSDetails(transactionId) else repository.getEFTNSDetails(
                            transactionId
                        )
                    transactionDetails.postValue(handleDetailsResponse(response))
                } catch (e: Exception) {
                    transactionDetails.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                transactionDetails.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleDetailsResponse(response: Response<Details>): Resource<Details> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleResponse(response: Response<RTGSListResponse>): Resource<RTGSListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}