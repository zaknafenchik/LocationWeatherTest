package com.example.locationweathertest.screen.listcity


import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.example.locationweathertest.LocationProvider
import com.example.locationweathertest.data.pojo.City
import com.example.locationweathertest.data.pojo.Location
import com.example.locationweathertest.data.repo.Repo
import com.example.locationweathertest.screen.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class CityListPresenter(private val repo: Repo) : BasePresenter<CityListView>() {
    companion object {
        private val TAG = CityListPresenter::class.simpleName
    }

    private var location: Location? = null

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe({ viewState.showCities(it) }, { Log.d(TAG, it.message) })
        )
    }

    fun onLoadWeather(city: City) {
        compositeDisposable.add(
            repo.loadWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showWeather(it) }, { Log.d(TAG, it.message) })
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}