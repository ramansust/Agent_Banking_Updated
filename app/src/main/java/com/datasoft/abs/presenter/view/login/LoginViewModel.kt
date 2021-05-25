package com.datasoft.abs.presenter.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//class LoginViewModel (repository: Repository) : BaseViewModel(repository) {
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val loginSuccess = MutableLiveData<Boolean>()
    private val message = MutableLiveData<String>()

    fun getLoginStatus(): LiveData<Boolean> = loginSuccess
    fun getLoginMessage(): LiveData<String> = message

    fun requestLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.performLogin(username, password)

                if (response.isSuccessful) {
                    val result = response.body()
                    repository.setAuthToken(result?.authToken ?: return@launch)
                    loginSuccess.postValue(true)
                    message.postValue("Successfully Logged in")
                } else {
                    loginSuccess.postValue(false)
                    message.postValue("Failed : " + response.code())
                }


            } catch (e: Exception) {
                loginSuccess.postValue(false)
                message.postValue("Something Went Wrong $e")
                e.printStackTrace()
            }
        }
    }

}
