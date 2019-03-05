package com.example.locationweathertest

import com.example.locationweathertest.data.pojo.Location
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

object LocationProvider {
    private val locationSubject = BehaviorSubject.create<Location>()
    private val searchRadius = PublishSubject.create<Boolean>()

    fun subscribeLocation(): Flowable<Location> {
        return locationSubject.toFlowable(BackpressureStrategy.DROP)
    }

    fun locationChanged(location: Location) {
        locationSubject.onNext(location)
    }

    fun searchRadiusChanged() {
        searchRadius.onNext(true)
    }

    fun subscribeRadiusChanged(): Flowable<Boolean> {
        return searchRadius.toFlowable(BackpressureStrategy.DROP)
    }
}
