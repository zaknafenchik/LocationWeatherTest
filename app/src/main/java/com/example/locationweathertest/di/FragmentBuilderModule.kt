package com.example.locationweathertest.di

import com.example.locationweathertest.screen.listcity.CityListFragment
import com.example.locationweathertest.screen.listcity.CityListModule
import com.example.locationweathertest.screen.map.MapFragment
import com.example.locationweathertest.screen.map.MapModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {
    @ContributesAndroidInjector(modules = [MapModule::class])
    abstract fun contributeMapFragmet(): MapFragment

    @ContributesAndroidInjector(modules = [CityListModule::class])
    abstract fun contributeListCityFragmet(): CityListFragment
}