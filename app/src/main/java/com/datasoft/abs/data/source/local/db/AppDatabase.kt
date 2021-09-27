package com.datasoft.abs.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.dao.customer.CustomerDao
import com.datasoft.abs.data.source.local.db.entity.account.*
import com.datasoft.abs.data.source.local.db.entity.customer.*

@Database(
    entities = [General::class, Personal::class, Nominee::class, Guardian::class, Address::class, Photo::class, Fingerprint::class, Document::class, RiskGrading::class, DocumentIdentification::class,
        Account::class, Customer::class, OtherFacilities::class, AccountNominee::class, NomineeGuardian::class, Introducer::class, TransactionProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun generalDao(): CustomerDao

    abstract fun accountDao(): AccountDao
}