package com.example.locationweathertest.screen.listcity

import com.example.locationweathertest.data.repo.Repo
import dagger.Module
import dagger.Provides

@Module
class CityListModule {

    @Provides
    fun providePresenter(repository: Repo) = CityListPresenter(repository)
}