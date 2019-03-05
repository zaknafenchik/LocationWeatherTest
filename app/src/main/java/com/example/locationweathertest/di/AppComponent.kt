package com.example.locationweathertest.di

import android.app.Application
import com.example.cityweather.di.NetworkModule
import com.example.locationweathertest.App
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ActivityBuilderModule::class]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

    fun inject(app: Application)
}