package com.datasoft.abs.presenter.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.datasoft.abs.R
import com.datasoft.abs.data.RepositoryImpl
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.utils.Photos
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresenterDi {

    @Provides
    @Singleton
    fun provideRepo(repositoryImpl: RepositoryImpl): Repository = repositoryImpl


//    @Singleton
//    @Provides
//    fun provideCustomerAdapter() = CustomerAdapter()

//    @Singleton
//    @Provides
//    fun provideDashboardAdapter(@ApplicationContext context: Context) = DashboardAdapter(context)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_photo_preview)
            .error(R.drawable.ic_photo_preview)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun providePhotoInstance(@ApplicationContext context: Context) = Photos(context)
}