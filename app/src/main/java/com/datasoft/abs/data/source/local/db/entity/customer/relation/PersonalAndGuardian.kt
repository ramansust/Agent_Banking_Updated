package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.Guardian
import com.datasoft.abs.data.source.local.db.entity.customer.Personal

data class PersonalAndGuardian(
    @Embedded val personal: Personal,
    @Relation(
        parentColumn = "id",
        entityColumn = "personalId"
    )
    val guardian: Guardian
)
