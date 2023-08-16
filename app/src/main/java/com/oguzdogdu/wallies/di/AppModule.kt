package com.oguzdogdu.wallies.di

import android.content.Context
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.ITooltipUtils
import com.oguzdogdu.wallies.util.TooltipUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCheckConnection(
        @ApplicationContext context: Context
    ): CheckConnection {
        return CheckConnection(context)
    }

    @Provides
    @Singleton
    fun provideTooltip(@ApplicationContext context: Context): ITooltipUtils {
        return TooltipUtils(context)
    }
}
