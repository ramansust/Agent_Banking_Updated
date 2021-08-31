package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.NO_INTERNET
import com.datasoft.abs.presenter.utils.Constant.SOMETHING_WRONG
import com.datasoft.abs.presenter.utils.Network
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

    private val searchData: MutableLiveData<Resource<String>> = MutableLiveData()
    fun getSearchData(): LiveData<Resource<String>> = searchData

    private val eftnData = MutableLiveData<Resource<RTGSListResponse>>()
    fun getEFTNData(): LiveData<Resource<RTGSListResponse>> = eftnData

    init {
        requestEFTNData(AccountRequest(1, status = "7"))
    }

    fun setSearchData(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchData.postValue(Resource.Success(search))
        }
    }

    private fun requestEFTNData(accountRequest: AccountRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            eftnData.postValue(Resource.Loading())

            if (network.isConnected()) {
                try {
                    val response = repository.getEFTNList(accountRequest)
                    eftnData.postValue(handleResponse(response))
                } catch (e: Exception) {
                    eftnData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                eftnData.postValue(
                    Resource.Error(
                        noInternet, null
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