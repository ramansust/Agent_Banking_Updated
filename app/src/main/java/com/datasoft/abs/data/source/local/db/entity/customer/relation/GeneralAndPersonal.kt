package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.General
import com.datasoft.abs.data.source.local.db.entity.customer.Personal

data class GeneralAndPersonal(
    @Embedded val general: General,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val personal: Personal
)
