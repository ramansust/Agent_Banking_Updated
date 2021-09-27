package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.DocumentIdentification
import com.datasoft.abs.data.source.local.db.entity.customer.General

data class GeneralAndDocumentIdentification(
    @Embedded val general: General,
    @Relation(
        parentColumn = "id",
        entityColumn = "generalId"
    )
    val documentIdentification: DocumentIdentification
)
