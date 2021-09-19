package com.datasoft.abs.domain.usecase

import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(userId: String, password: String): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.loading(null))
            val login = repository.performLogin(userId, password)
            emit(Resource.success(login.body()))
        } catch (e: HttpException) {
            emit(Resource.error(e.localizedMessage ?: "An unexpected error occurred", null))
        } catch (e: IOException) {
            emit(Resource.error("Couldn't reach server. Check your internet connection.", null))
        }
    }
}
