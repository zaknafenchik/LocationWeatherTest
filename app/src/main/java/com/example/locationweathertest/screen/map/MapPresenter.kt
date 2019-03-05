package com.example.locationweathertest.screen.map

import com.arellomobile.mvp.InjectViewState
import com.example.locationweathertest.LocationProvider
import com.example.locationweathertest.data.pojo.City
import com.example.locationweathertest.data.pojo.Location
import com.example.locationweathertest.data.repo.Repo
import com.example.locationweathertest.screen.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


@InjectViewState
class MapPresenter(private val repo: Repo) : BasePresenter<MapView>() {

    private var location: Location? = null
    private var mapReady = false
    private val cities = arrayListOf<City>()

    init {
        subscribeLocationChanged()
        subscribeSearchRadiusChanged()
    }

    private fun subscribeLocationChanged() {
        compositeDisposable.add(
            LocationProvider.subscribeLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loadCity(it) }, {})
        )
    }

    private fun subscribeSearchRadiusChanged() {
        compositeDisposable.add(
            LocationProvider.subscribeRadiusChanged()
                .subscribe { location?.let { loadCity(it) } }
        )
    }

    private fun loadCity(location: Location) {
        this.location = location
        compositeDisposable.add(
            repo.loadNearbyCities(location)
                .map {
                    for (city in it) {
                        repo.loadWeather(city)
                            .subscribe { t -> city.weather = t.weather }
                    }
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe({
                    if (mapReady) {
                        viewState.showCities(it)
                    } else {
                        cities.clear()
                        cities.addAll(it)
                    }
                }, { })
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onMapReady() {
        mapReady = true
        if (cities.isNotEmpty()) {
            viewState.showCities(cities)
        }
    }
}