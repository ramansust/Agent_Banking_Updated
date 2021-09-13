package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.introducer.IntroducerInfo
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class IntroducerViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.SEARCH_EMPTY) private val searchEmpty: String,
) : ViewModel() {

    private val introducer = MutableLiveData<Event<Resource<IntroducerInfo>>>()
    fun getIntroducerData(): LiveData<Event<Resource<IntroducerInfo>>> =
        introducer

    private val relationId = MutableLiveData<Int>()
    fun getRelationId(): LiveData<Int> = relationId

    fun introducerData(accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            if (accountNo.isEmpty()) {
                introducer.postValue(
                    Event(
                        Resource.error(
                            searchEmpty, null
                        )
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
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                introducer.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleIntroducerResponse(response: Response<IntroducerInfo>): Event<Resource<IntroducerInfo>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

    fun setRelationId(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            relationId.postValue(value)
        }
    }
}