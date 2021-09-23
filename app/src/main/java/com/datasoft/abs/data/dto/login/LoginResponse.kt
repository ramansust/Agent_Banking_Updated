package com.datasoft.abs.data.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val accountNumber: Int?,
    @SerializedName("auth_token")
    val authToken: String?,
    val branchId: Int?,
    val branchName: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    val message: Any?,
    val roleId: Int?,
    val roleName: String?,
    val statusCode: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("UserName")
    val userName: String?,
    val fingerData: String?,
    @SerializedName("ProfilePhoto")
    val profilePhoto: String?
)
