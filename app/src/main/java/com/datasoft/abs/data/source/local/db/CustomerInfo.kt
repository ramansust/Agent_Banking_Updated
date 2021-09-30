package com.datasoft.abs.data.source.local.db

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerInfo @Inject constructor() {
    var customerId: Int = 1
    var personalId: Int = 1
}