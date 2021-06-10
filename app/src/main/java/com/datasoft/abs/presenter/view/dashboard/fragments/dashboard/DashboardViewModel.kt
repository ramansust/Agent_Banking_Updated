package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
) : ViewModel() {

    private val dayCount = MutableLiveData<Int>()

    fun setDayCount(count: Int) {
        dayCount.postValue(count)
    }

    fun getDayCount(): LiveData<Int> = dayCount
}