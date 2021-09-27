package com.datasoft.abs.data.source.local.db.entity.account.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.account.Account
import com.datasoft.abs.data.source.local.db.entity.account.AccountNominee
import com.datasoft.abs.data.source.local.db.entity.account.Introducer
import com.datasoft.abs.data.source.local.db.entity.account.NomineeGuardian

data class AccountNomineeAndNomineeGuardian(
    @Embedded val accountNominee: AccountNominee,
    @Relation(
        parentColumn = "id",
        entityColumn = "nomineeId"
    )
    val nomineeGuardian: NomineeGuardian
)
