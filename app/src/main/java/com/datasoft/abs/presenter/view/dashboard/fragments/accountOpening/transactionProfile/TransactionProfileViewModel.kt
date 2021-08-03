package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.TpDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionProfileViewModel @Inject constructor(

): ViewModel() {

    private val transactionProfile = MutableLiveData<List<TpDetail>>()
    fun getTransactionProfile(): LiveData<List<TpDetail>> = transactionProfile

    fun setTransactionProfile(list: List<TpDetail>) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionProfile.postValue(list)
        }
    }
}