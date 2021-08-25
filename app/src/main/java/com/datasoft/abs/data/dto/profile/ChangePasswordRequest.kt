package com.datasoft.abs.data.dto.profile

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String,
)