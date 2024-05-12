package com.training.ecommerce.di

import com.training.ecommerce.data.datasource.networking.CloudFunctionAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun provideCloudFunctionsApi(): CloudFunctionAPI {
        return CloudFunctionAPI.create()
    }
}