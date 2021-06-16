package com.datasoft.abs.domain.usecase

/*
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.domain.executor.PostExecutorThread
import com.datasoft.abs.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: Repository,
    private val postExecutorThread: PostExecutorThread
): FlowUseCase<String, Response<LoginResponse>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecutorThread.io

    override fun execute(params: String?): Flow<Response<LoginResponse>> {
        return repository.performLogin(params?.get(0).toString(), params?.get(2).toString())
    }
}*/
