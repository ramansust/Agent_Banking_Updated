package com.datasoft.abs.presenter.view.dashboard.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.profile.ChangePasswordRequest
import com.datasoft.abs.data.dto.profile.ChangePasswordResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.PASSWORD_REGEX
import com.datasoft.abs.presenter.utils.Event
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
) : ViewModel() {

    private val changePassword = MutableLiveData<Event<Resource<ChangePasswordResponse>>>()
    fun getChangePassword(): LiveData<Event<Resource<ChangePasswordResponse>>> = changePassword

    fun changePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {

            changePassword.postValue(Event(Resource.loading(null)))

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                changePassword.postValue(
                    Event(Resource.error(
                        fieldEmpty, null
                    ))
                )

                return@launch
            } else if (newPassword != confirmNewPassword) {
                changePassword.postValue(
                    Event(Resource.error(
                        "New password and the confirm password are not same!", null
                    ))
                )

                return@launch
            } else if (!PASSWORD_REGEX.toRegex().matches(newPassword)) {
                changePassword.postValue(
                    Event(Resource.error(
                        "Follow the instructions to make the valid password!", null
                    ))
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
                    changePassword.postValue(Event(handleResponse(response)))
                } catch (e: Exception) {
                    changePassword.postValue(
                        Event(Resource.error(
                            somethingWrong, null
                        ))
                    )
                    e.printStackTrace()
                }
            } else {
                changePassword.postValue(
                    Event(Resource.error(
                        noInternet, null
                    ))
                )
            }
        }
    }

    private fun handleResponse(response: Response<ChangePasswordResponse>): Resource<ChangePasswordResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.success(resultResponse)
            }
        }
        return Resource.error(response.message(), null)
    }
}