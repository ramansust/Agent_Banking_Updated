package com.datasoft.abs.data.source.local.db.repo

import androidx.annotation.WorkerThread
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import com.datasoft.abs.data.source.local.db.interfaceDAO.GeneralInfoDao
import javax.inject.Inject

class GeneralInfoRepo @Inject constructor(private val generalInfoDao: GeneralInfoDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: List<GeneralInfo> = generalInfoDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(generalInfo: GeneralInfo) {
        generalInfoDao.insert(generalInfo)
    }
}