package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

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
class AddressViewModel @Inject constructor() : ViewModel() {

    private val saveData = MutableLiveData<ArrayList<String>>()
    fun getSavedData(): LiveData<ArrayList<String>> = saveData

    private val sendMessage = MutableLiveData<Resource<String>>()
    fun getMessage(): LiveData<Resource<String>> = sendMessage

    fun checkData(flatNo: String) {
        viewModelScope.launch(Dispatchers.IO) {

            sendMessage.postValue(Resource.Loading())

            if (flatNo.isEmpty()) {
                sendMessage.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            sendMessage.postValue(Resource.Success(flatNo))
        }
    }

    fun notifyData(flatNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<String> = ArrayList()
            list.add(flatNo)

            saveData.value?.let {
                list.addAll(list.size - 1, it)
            }

            saveData.postValue(list)
        }
    }

}