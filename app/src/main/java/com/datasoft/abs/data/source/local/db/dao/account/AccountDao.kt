package com.datasoft.abs.data.source.local.db.dao.account

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datasoft.abs.data.source.local.db.entity.account.*
import com.datasoft.abs.data.source.local.db.entity.account.relation.*

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: Account): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertCustomers(customer: List<Customer>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOthersFacilities(otherFacilities: List<OtherFacilities>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNominee(accountNominee: AccountNominee): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNomineeGuardian(nomineeGuardian: NomineeGuardian)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntroducer(introducer: Introducer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransactionProfiles(transactionProfile: List<TransactionProfile>)

    @Query("UPDATE account_other_facilities SET isChecked = :value WHERE id = :id")
    fun updateOtherFacility(value: Boolean, id: Int)

    @Update
    fun updateTransactionProfile(transactionProfile: TransactionProfile)

    @Query("SELECT * FROM account_account_info")
    fun getAll(): LiveData<List<Account>>

    @Transaction
    @Query("SELECT * FROM account_account_info WHERE id = :accountId")
    fun getAccountWithCustomers(accountId: Int): AccountWithCustomers

    @Transaction
    @Query("SELECT * FROM account_account_info WHERE id = :accountId")
    fun getAccountWithOthersFacilities(accountId: Int): AccountWithOtherFacilities

    @Query("SELECT * FROM account_other_facilities WHERE accountId =:accountId")
    fun getOtherFacilities(accountId: Int): LiveData<List<OtherFacilities>>

    @Transaction
    @Query("SELECT * FROM account_account_info WHERE id = :accountId")
    fun getAccountWithNominees(accountId: Int): AccountWithNominees

    @Transaction
    @Query("SELECT * FROM account_nominee_info WHERE id = :nomineeId")
    fun getNomineeAndGuardian(nomineeId: Int): AccountNomineeAndNomineeGuardian

    @Transaction
    @Query("SELECT * FROM account_account_info WHERE id = :accountId")
    fun getAccountAndIntroducer(accountId: Int): AccountAndIntroducer

    @Transaction
    @Query("SELECT * FROM account_account_info WHERE id = :accountId")
    fun getAccountWithTransactionProfiles(accountId: Int): AccountWithTransactionProfiles

    @Query("DELETE FROM account_account_info WHERE id = :accountId")
    fun deleteAccount(accountId: Int)

    @Query("DELETE FROM account_nominee_info WHERE id = :nomineeId")
    fun deleteNominee(nomineeId: Int)
}