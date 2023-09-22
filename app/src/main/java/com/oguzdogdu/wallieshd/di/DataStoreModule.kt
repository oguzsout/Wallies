package com.oguzdogdu.wallieshd.di

import android.content.Context
import com.oguzdogdu.data.repository.DataStoreImpl
import com.oguzdogdu.domain.repository.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Singleton
    @Provides
    fun providesDatastoreRepo(
        @ApplicationContext context: Context
    ): DataStore = DataStoreImpl(context)
}
