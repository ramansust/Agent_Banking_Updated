package com.datasoft.abs.presenter.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.utils.Network
import com.datasoft.abs.presenter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

//class LoginViewModel (repository: Repository) : BaseViewModel(repository) {
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: Network
) : ViewModel() {

    private val login: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun getLoginData(): LiveData<Resource<LoginResponse>> = login

    fun requestLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            login.postValue(Resource.Loading())

            if (username.isEmpty() || password.isEmpty()) {
                login.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            if (networkHelper.isConnected()) {
                networkRequestForLogin(username, password)
            } else {
                login.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                repository.setAuthToken(resultResponse.authToken ?: "")
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun networkRequestForLogin(username: String, password: String) = viewModelScope.launch {
        try {
            val response = repository.performLogin(username, password)
            login.postValue(handleLoginResponse(response))
        } catch (e: Exception) {
            login.postValue(
                Resource.Error(
                    "Something went wrong!", null
                )
            )
            e.printStackTrace()
        }

    }

}
