package com.example.locationweathertest.screen.map

import com.example.locationweathertest.data.repo.Repo
import dagger.Module
import dagger.Provides

@Module
class MapModule {

    @Provides
    fun provideMapPresenter(repo: Repo) = MapPresenter(repo)
}