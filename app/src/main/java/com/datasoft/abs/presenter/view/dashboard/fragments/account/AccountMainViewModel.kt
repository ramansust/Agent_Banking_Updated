package com.datasoft.abs.presenter.view.dashboard.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountMainViewModel @Inject constructor(
) : ViewModel() {

    private val searchData: MutableLiveData<String> = MutableLiveData()

    fun getSearchData(): LiveData<String> = searchData

    fun setSearchData(search: String) {
        searchData.postValue(search)
    }
}