package com.datasoft.abs.presenter.di

import android.content.Context
import com.datasoft.abs.R
import com.datasoft.abs.presenter.utils.Constant.FIELD_EMPTY
import com.datasoft.abs.presenter.utils.Constant.ID_EMPTY
import com.datasoft.abs.presenter.utils.Constant.NO_INTERNET
import com.datasoft.abs.presenter.utils.Constant.SEARCH_EMPTY
import com.datasoft.abs.presenter.utils.Constant.SOMETHING_WRONG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object MessageDi {

    @Provides
    @ViewModelScoped
    @Named(NO_INTERNET)
    fun provideNoInternetMessage(
        @ApplicationContext context: Context
    ) = context.getString(R.string.no_internet)

    @Provides
    @ViewModelScoped
    @Named(SOMETHING_WRONG)
    fun provideWrongMessage(
        @ApplicationContext context: Context
    ) = context.getString(R.string.something_wrong)

    @Provides
    @ViewModelScoped
    @Named(FIELD_EMPTY)
    fun provideEmptyFieldMessage(
        @ApplicationContext context: Context
    ) = context.getString(R.string.filed_empty)

    @Provides
    @ViewModelScoped
    @Named(SEARCH_EMPTY)
    fun provideEmptySearchMessage(
        @ApplicationContext context: Context
    ) = context.getString(R.string.search_empty)

    @Provides
    @ViewModelScoped
    @Named(ID_EMPTY)
    fun provideEmptyIDMessage(
        @ApplicationContext context: Context
    ) = context.getString(R.string.id_empty)
}