package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EFTNViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Transaction Fragment"
    }
    val text: LiveData<String> = _text
}