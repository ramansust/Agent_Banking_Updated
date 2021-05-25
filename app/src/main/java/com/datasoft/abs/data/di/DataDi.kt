package com.datasoft.abs.data.di

import android.content.Context
import androidx.room.Room
import com.datasoft.abs.data.source.local.DataManager
import com.datasoft.abs.data.source.local.db.AppDatabase
import com.datasoft.abs.data.source.local.db.interfaceDAO.GeneralInfoDao
import com.datasoft.abs.data.source.local.db.repo.GeneralInfoRepo
import com.datasoft.abs.data.source.remote.RestRemoteDataSource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providesRemoteDataSource() = RestRemoteDataSource(
        Constant.RemoteUrl,
        Constant.ConnectTimeout,
        Constant.ReadTimeout,
        Constant.WriteTimeout
    )

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDataManager(@ApplicationContext context: Context) = DataManager(context)

    @Singleton
    @Provides
    fun providesGeneralInfoDao(appDatabase: AppDatabase): GeneralInfoDao = appDatabase.generalDao()

    @Singleton
    @Provides
    fun providesUserRepository(generalInfoDao: GeneralInfoDao) : GeneralInfoRepo
            = GeneralInfoRepo(generalInfoDao)
}