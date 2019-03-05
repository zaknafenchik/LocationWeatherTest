package com.example.locationweathertest.screen.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.cityweather.di.Injectable
import com.example.locationweathertest.LocationProvider
import com.example.locationweathertest.R
import com.example.locationweathertest.data.pojo.Location
import com.example.locationweathertest.data.repo.Repo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var repo: Repo

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val rxPermissions = RxPermissions(this)

    private val locationRequest = LocationRequest()
        .setSmallestDisplacement(100.toFloat())
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private var disposable: Disposable? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            for (location in locationResult.locations) {
                LocationProvider.locationChanged(Location(location.latitude, location.longitude))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = FusedLocationProviderClient(this)
        setupTabView()
        setupViews()
    }

    private fun setupViews() {
        setupTabView()
        etRadius.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnAccept.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        btnAccept.setOnClickListener {
            btnAccept.visibility = View.INVISIBLE
            repo.setRadius(etRadius.text.toString().toInt())

            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun setupTabView() {
        viewPager.adapter = PageAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        disposable = rxPermissions.request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            .subscribe {
                if (it) {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
        disposable?.dispose()
    }
}
