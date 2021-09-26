package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.Fingerprint
import com.datasoft.abs.data.source.local.db.entity.customer.General

data class GeneralAndFingerprint(
    @Embedded val general: General,
    @Relation(
        parentColumn = "id",
        entityColumn = "generalId"
    )
    val fingerprint: Fingerprint
)
