package com.oguzdogdu.wallies.di

import android.content.Context
import com.oguzdogdu.wallies.util.CheckConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideCheckConnection(
        @ApplicationContext context: Context
    ) : CheckConnection {
        return CheckConnection(context)
    }
}