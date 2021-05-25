package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val dashboardData = MutableLiveData<DashboardResponse?>()

    fun getDashboardData(): MutableLiveData<DashboardResponse?> = dashboardData

    fun requestDashboardData(branchId: Int, fromDate: String, toDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getDashboardData(branchId, fromDate, toDate)

                if (response.isSuccessful) {
                    val result = response.body()
                    dashboardData.postValue(result)
                } else {
                    dashboardData.postValue(null)
                }

            } catch (e: Exception) {
                dashboardData.postValue(null)
                e.printStackTrace()
            }
        }
    }

}