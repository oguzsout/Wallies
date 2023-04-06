package com.oguzdogdu.wallies.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.oguzdogdu.wallies.util.CheckConnection
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideCheckConnection(
        @ApplicationContext context: Context
    ) : CheckConnection {
        return CheckConnection(context)
    }
}