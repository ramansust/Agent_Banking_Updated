package com.datasoft.abs.presenter.base

import androidx.lifecycle.ViewModel
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(val repository: Repository) : ViewModel() {
}