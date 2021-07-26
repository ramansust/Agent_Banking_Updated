package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.fingerprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FingerprintViewModel @Inject constructor(

) : ViewModel() {

    private val fingerList = MutableLiveData<List<String>>()
    fun getFingerList(): LiveData<List<String>> = fingerList

    fun setFinger(index: Int, value: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val list: ArrayList<String> = ArrayList()
            list.add(value)
            fingerList.value?.let {
                list.addAll(it)
            }

            fingerList.postValue(list)
        }
    }

}