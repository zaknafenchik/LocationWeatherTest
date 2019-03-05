package com.example.locationweathertest.di

import android.content.Context
import com.example.locationweathertest.App
import com.example.locationweathertest.data.api.GeoCityApi
import com.example.locationweathertest.data.api.WeatherApi
import com.example.locationweathertest.data.repo.Repo
import com.example.locationweathertest.data.repo.RepoImpl
import com.example.locationweathertest.di.qualifier.ApplicationContext
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @ApplicationContext
    @Singleton
    fun provideContext(app: App): Context = app

    @Provides
    @Singleton
    fun provideRepository(geoCityApi: GeoCityApi, weatherApi: WeatherApi): Repo {
        return RepoImpl(geoCityApi, weatherApi)
    }
}