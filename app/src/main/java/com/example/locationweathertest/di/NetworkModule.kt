package com.example.cityweather.di

import android.content.Context
import com.example.locationweathertest.data.api.GeoCityApi
import com.example.locationweathertest.data.api.WeatherApi
import com.example.locationweathertest.di.qualifier.ApplicationContext
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    internal fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(context))
            .build()
    }

    @Singleton
    @Provides
    @Named("weather_retrofit")
    internal fun provideWeatherRetrofit(
        client: OkHttpClient, @Named("weather_base_url") baseUrl: String,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    @Named("geo_city_retrofit")
    internal fun provideGoogleRetrofit(
        client: OkHttpClient, @Named("geo_city_base_url") baseUrl: String,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    internal fun provideApiService(@Named("weather_retrofit") retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideGoogleApiService(@Named("geo_city_retrofit") retrofit: Retrofit): GeoCityApi {
        return retrofit.create(GeoCityApi::class.java)
    }

    @Singleton
    @Named("weather_base_url")
    @Provides
    internal fun provideWeatherUrl(): String {
        return "http://api.openweathermap.org/data/2.5/"
    }

    @Singleton
    @Named("geo_city_base_url")
    @Provides
    internal fun provideGoooglePlaceUrl(): String {
        return "http://gd.geobytes.com/"
    }
}
