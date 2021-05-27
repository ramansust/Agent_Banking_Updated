package com.datasoft.abs.data

import androidx.lifecycle.LiveData
import com.datasoft.abs.data.dto.dashboard.DashboardRequest
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.login.LoginRequest
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import com.datasoft.abs.data.source.local.db.dao.GeneralInfoDao
import com.datasoft.abs.data.source.remote.JwtHelper
import com.datasoft.abs.data.source.remote.RestRemoteDataSource
import com.datasoft.abs.domain.Repository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    remoteDataSource: RestRemoteDataSource,
    private val jwtHelper: JwtHelper,
    private val generalInfoDao: GeneralInfoDao
) : Repository {
    private val restApiService by lazy { remoteDataSource.restApiService }

    override suspend fun performLogin(userName: String, password: String): Response<LoginResponse> {
        return restApiService.performLogin(LoginRequest(userName, password))
    }

    override fun setAuthToken(token: String) {
        jwtHelper.jwtToken = token
    }

    override suspend fun getDashboardData(
        branchId: Int,
        fromDate: String,
        toDate: String
    ): Response<DashboardResponse> {
        return restApiService.getDashboardData(DashboardRequest(branchId, fromDate, toDate))
    }

    override suspend fun insert(generalInfo: GeneralInfo) {
        generalInfoDao.insert(generalInfo)
    }

    override fun getAll(): LiveData<List<GeneralInfo>> {
        return generalInfoDao.getAll()
    }
}