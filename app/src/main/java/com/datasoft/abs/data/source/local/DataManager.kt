package com.datasoft.abs.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataManager(context: Context) {

    private val dataStore = context.createDataStore("data_prefs")

    companion object {
        val USER_NAME = preferencesKey<String>("USER_NAME")
    }

    suspend fun storeData(name: String) {
        dataStore.edit {
            it[USER_NAME] = name
        }
    }

    val userNameFlow: Flow<String> = dataStore.data.map {
        it[USER_NAME] ?: ""
    }

}