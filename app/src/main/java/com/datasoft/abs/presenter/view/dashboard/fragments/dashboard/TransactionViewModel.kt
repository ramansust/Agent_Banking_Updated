package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
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
class TransactionViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
) : ViewModel() {

    private val dashboardData = MutableLiveData<Event<Resource<DashboardResponse>>>()
    fun getDashboardData(): LiveData<Event<Resource<DashboardResponse>>> = dashboardData

    fun requestDashboardData(dayNo: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getDashboardData(dayNo)
                    dashboardData.postValue(handleDashboardResponse(response))
                } catch (e: Exception) {
                    dashboardData.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                dashboardData.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleDashboardResponse(response: Response<DashboardResponse>): Event<Resource<DashboardResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

}