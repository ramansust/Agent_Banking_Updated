package com.datasoft.abs.data.source.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtInterceptor @Inject constructor(private val jwtHelper: JwtHelper) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("jwt_value", "____${jwtHelper.jwtToken}")
        val jwtToken: String = jwtHelper.jwtToken
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        if (jwtToken.isEmpty()) return chain.proceed(builder.build())
        builder.addHeader("Authorization", jwtToken)
        val response = chain.proceed(builder.build())
//        if (response.code == ConstantValues.StatusCodes.TOKEN_EXPIRED_CODE) {
//            JCHApplication.getInstance().forceLogout()
//        }
        return response
    }
}