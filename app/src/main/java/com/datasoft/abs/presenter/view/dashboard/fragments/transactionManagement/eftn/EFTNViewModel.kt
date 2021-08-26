package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EFTNViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val details: MutableLiveData<Int> = MutableLiveData()
    fun getDetailsData(): LiveData<Int> = details

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    private val eftnData = MutableLiveData<Resource<RTGSListResponse>>()
    fun getEFTNData(): LiveData<Resource<RTGSListResponse>> = eftnData

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

    fun requestEFTNData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            eftnData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getEFTNList(accountRequest)
                    eftnData.postValue(handleResponse(response))
                } catch (e: Exception) {
                    eftnData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                eftnData.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
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