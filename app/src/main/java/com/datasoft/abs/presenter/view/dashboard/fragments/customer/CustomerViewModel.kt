package com.datasoft.abs.presenter.view.dashboard.fragments.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor() : ViewModel() {

    private val currentStep: MutableLiveData<Int> = MutableLiveData()
    fun getCurrentStep(): LiveData<Int> = currentStep

    fun requestCurrentStep(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentStep.postValue(index)
        }
    }

}
