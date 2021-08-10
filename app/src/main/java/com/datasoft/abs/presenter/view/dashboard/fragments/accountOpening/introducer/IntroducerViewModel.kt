package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.introducer.IntroducerInfo
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class IntroducerViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val introducer = MutableLiveData<Resource<IntroducerInfo>>()
    fun getIntroducerData(): LiveData<Resource<IntroducerInfo>> =
        introducer

    private val relationId = MutableLiveData<Int>()
    fun getRelationId(): LiveData<Int> = relationId

    fun introducerData(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if(accountNo.isEmpty()) {
                introducer.postValue(
                    Resource.Error(
                        "Search filed must not be empty!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getIntroducerData(accountNo)
                    introducer.postValue(handleIntroducerResponse(response))
                } catch (e: Exception) {
                    introducer.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                introducer.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleIntroducerResponse(response: Response<IntroducerInfo>): Resource<IntroducerInfo> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun setRelationId(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            relationId.postValue(value)
        }
    }
}