package com.datasoft.abs.data.source.local.db.dao.account

import androidx.lifecycle.LiveData
import androidx.room.*
import com.datasoft.abs.data.source.local.db.entity.account.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account_account_info")
    fun getAll(): LiveData<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account)

    @Delete
    fun delete(account: Account)
}