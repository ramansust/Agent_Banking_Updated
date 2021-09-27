package com.datasoft.abs.data.source.local.db.entity.customer.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.customer.General
import com.datasoft.abs.data.source.local.db.entity.customer.RiskGrading

data class GeneralAndRiskGrading(
    @Embedded val general: General,
    @Relation(
        parentColumn = "id",
        entityColumn = "generalId"
    )
    val riskGrading: RiskGrading
)
