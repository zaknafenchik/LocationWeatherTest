package com.example.locationweathertest.screen.map


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.cityweather.di.Injectable
import com.example.locationweathertest.R
import com.example.locationweathertest.data.pojo.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.marker.view.*
import javax.inject.Inject


class MapFragment : MvpAppCompatFragment(), Injectable, OnMapReadyCallback, MapView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MapPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    private fun initMap() {
        val mapFragment =
            activity?.fragmentManager?.findFragmentById(R.id.map) as com.google.android.gms.maps.MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        presenter.onMapReady()
    }

    override fun showProgress(show: Boolean) {
        progressBar.visibility = when (show) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun showCities(list: List<City>) {
        val builder = LatLngBounds.Builder()
        for (city in list) {
            val view = getMarkerView(city)
            val latLng = LatLng(city.location.lat, city.location.lng)
            mMap?.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(view)))
            )
            builder.include(latLng)
        }
        mMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 2))
    }

    private fun getMarkerView(city: City): View {
        val inflater = activity!!
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.marker, null)
        view.tvTemp.text = city.weather?.temperature.toString()
        view.tvCityName.text = city.name
        return view
    }

    private fun loadBitmapFromView(view: View): Bitmap {
        view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val b = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(c)
        return b
    }
}
