package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.Nominee
import com.datasoft.abs.data.source.local.db.entity.customer.Personal

data class PersonalAndNominee(
    @Embedded val personal: Personal,
    @Relation(
        parentColumn = "id",
        entityColumn = "personalId"
    )
    val nominee: Nominee
)
