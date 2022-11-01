package com.nowiczenko.andrzej.biblioteka.di


import com.nowiczenko.andrzej.biblioteka.retrofit.MyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): MyApi {
        return MyApi.invoke()
    }

}
