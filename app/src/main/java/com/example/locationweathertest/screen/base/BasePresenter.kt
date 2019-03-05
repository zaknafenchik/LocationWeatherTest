package com.example.locationweathertest.screen.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : MvpView> : MvpPresenter<V>() {
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}