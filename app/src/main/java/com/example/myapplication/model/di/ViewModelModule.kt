package com.example.myapplication.model.di

import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.model.service.RetrofitServiceInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): ApiInterface {
        return RetrofitServiceInstance.getInstance().create(ApiInterface::class.java)
    }
}
