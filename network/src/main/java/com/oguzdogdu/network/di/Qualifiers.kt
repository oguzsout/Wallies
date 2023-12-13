package com.oguzdogdu.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WalliesRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WalliesOkHttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InterceptorLogging