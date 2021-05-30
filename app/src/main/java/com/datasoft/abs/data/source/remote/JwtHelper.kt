package com.datasoft.abs.data.source.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtHelper @Inject constructor() {
    var jwtToken: String = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJlbXJveiIsImp0aSI6ImExZTIzOTg3LThhMDktNDdmZS05NGVkLTY1MTJlMGRlODExMCIsImlhdCI6MTYyMjM1NDQ3NywiSnd0Um9sZSI6Ikp3dENsYWltIiwiVXNlcklkIjoiOSIsIkJyYW5jaElkIjoiMjI3MSIsIlJvbGVJZCI6IjUiLCJQZXJtaXR0ZWRBY2NvdW50SWQiOiIxMzA1MzAiLCJuYmYiOjE2MjIzNTQ0NzcsImV4cCI6MTYyMjQxNDQ3NywiaXNzIjoid2ViQXBpIiwiYXVkIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6NDQzODEifQ.AEoAflOizA9VLjYtoZnnMyKpz3-CHheO6UM6NaJOlv0"
        set(value) {
            field = "Bearer $value"
        }

}