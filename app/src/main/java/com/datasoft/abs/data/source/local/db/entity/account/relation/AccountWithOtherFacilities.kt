package com.datasoft.abs.data.source.local.db.entity.account.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.account.Account
import com.datasoft.abs.data.source.local.db.entity.account.AccountNominee
import com.datasoft.abs.data.source.local.db.entity.account.OtherFacilities

data class AccountWithOtherFacilities(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val otherFacilities: List<OtherFacilities>
)
