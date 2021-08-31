package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
) : ViewModel() {

    private val dayCount = MutableLiveData<Resource<Int>>()
    fun getDayCount(): LiveData<Resource<Int>> = dayCount

    fun setDayCount(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dayCount.postValue(Resource.Success(count))
        }
    }
}