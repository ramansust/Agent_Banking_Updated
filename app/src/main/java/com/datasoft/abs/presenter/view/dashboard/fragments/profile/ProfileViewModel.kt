package com.datasoft.abs.presenter.view.dashboard.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.profile.ChangePasswordRequest
import com.datasoft.abs.data.dto.profile.ChangePasswordResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.PASSWORD_REGEX
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val changePassword = MutableLiveData<Resource<ChangePasswordResponse>>()
    fun getChangePassword(): LiveData<Resource<ChangePasswordResponse>> = changePassword

    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {

            changePassword.postValue(Resource.Loading())

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                changePassword.postValue(
                    Resource.Error(
                        "Field must not be empty!", null
                    )
                )

                return@launch
            } else if (newPassword != confirmNewPassword) {
                changePassword.postValue(
                    Resource.Error(
                        "New password and the confirm password are not same!", null
                    )
                )

                return@launch
            } else if (!PASSWORD_REGEX.toRegex().matches(newPassword)) {
                changePassword.postValue(
                    Resource.Error(
                        "Follow the instructions to make the valid password!", null
                    )
                )

                return@launch
            }

            if (network.isConnected()) {
                try {
                    val response = repository.getChangePassword(
                        ChangePasswordRequest(
                            currentPassword,
                            newPassword,
                            confirmNewPassword
                        )
                    )
                    changePassword.postValue(handleResponse(response))
                } catch (e: Exception) {
                    changePassword.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                changePassword.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<ChangePasswordResponse>): Resource<ChangePasswordResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}