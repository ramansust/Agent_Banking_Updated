package com.datasoft.abs.data.source.local.db.entity.account.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.datasoft.abs.data.source.local.db.entity.account.Account
import com.datasoft.abs.data.source.local.db.entity.account.AccountNominee

data class AccountWithNominees(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val nominees: List<AccountNominee>
)
