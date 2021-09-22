package com.datasoft.abs.data.dto.profile

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String,
)