package com.datasoft.abs.data.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("AccountNumber")
    val accountNumber: Int?,
    @SerializedName("auth_token")
    val authToken: String?,
    @SerializedName("BranchId")
    val branchId: Int?,
    @SerializedName("BranchName")
    val branchName: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("Message")
    val message: Any?,
    @SerializedName("RoleId")
    val roleId: Int?,
    @SerializedName("RoleName")
    val roleName: String?,
    @SerializedName("StatusCode")
    val statusCode: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("UserName")
    val userName: String?
)
