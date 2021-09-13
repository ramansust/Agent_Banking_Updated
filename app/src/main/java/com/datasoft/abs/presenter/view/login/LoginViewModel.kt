package com.datasoft.abs.presenter.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

//class LoginViewModel (repository: Repository) : BaseViewModel(repository) {
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val login: MutableLiveData<Event<Resource<LoginResponse>>> = MutableLiveData()
    fun getLoginData(): LiveData<Event<Resource<LoginResponse>>> = login

    fun requestLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            login.postValue(Event(Resource.loading(null)))

            if (username.isEmpty() || password.isEmpty()) {
                login.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
                    )
                )
                return@launch
            }

            if (networkHelper.isConnected()) {
                networkRequestForLogin(username, password)
            } else {
                login.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Event<Resource<LoginResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                repository.setAuthToken(resultResponse.authToken ?: "")
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error("Error ${response.message()}", null))
//        return Resource.Error("Something went wrong!")
    }

    private fun networkRequestForLogin(username: String, password: String) = viewModelScope.launch {
        try {
            val response = repository.performLogin(username, password)
            login.postValue(handleLoginResponse(response))
        } catch (e: Exception) {
            login.postValue(
                Event(
                    Resource.error(
                        somethingWrong, null
                    )
                )
            )
            e.printStackTrace()
        }

    }

}
