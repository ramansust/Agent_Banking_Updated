package com.datasoft.abs.data.source.remote

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JwtHelper @Inject constructor() {
    var jwtToken: String? = null
        set(value) {
            field = "Bearer $value"
        }

}