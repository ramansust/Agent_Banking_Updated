package com.datasoft.abs.data.di

import android.content.Context
import androidx.room.Room
import com.datasoft.abs.data.source.local.DataManager
import com.datasoft.abs.data.source.local.db.AppDatabase
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.dao.customer.CustomerDao
import com.datasoft.abs.data.source.remote.JwtInterceptor
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
object DataModule {

    @Singleton
    @Provides
    fun providesRemoteDataSource(jwtInterceptor: JwtInterceptor) = RestRemoteDataSource(
        Constant.ConnectTimeout,
        Constant.ReadTimeout,
        Constant.WriteTimeout,
        jwtInterceptor
    )

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideDataManager(@ApplicationContext context: Context) = DataManager(context)

    @Singleton
    @Provides
    fun providesCustomerDao(appDatabase: AppDatabase): CustomerDao = appDatabase.customerDao()

    @Singleton
    @Provides
    fun providesAccountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()
}