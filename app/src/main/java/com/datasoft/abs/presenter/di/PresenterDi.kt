package com.datasoft.abs.presenter.di

import com.datasoft.abs.data.RepositoryImpl
import com.datasoft.abs.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresenterDi {

    @Binds
    @Singleton
    abstract fun provideRepo(repositoryImpl: RepositoryImpl): Repository
}