package com.datasoft.abs.presenter.states

sealed class CommonState {
    object Fetching : CommonState()
    object Complete : CommonState()
    data class Failed(val message: String, val exception: Exception) : CommonState()
}