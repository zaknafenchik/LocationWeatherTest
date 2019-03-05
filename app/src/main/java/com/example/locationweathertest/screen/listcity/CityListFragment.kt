package com.example.locationweathertest.screen.listcity


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.cityweather.di.Injectable
import com.example.locationweathertest.R
import com.example.locationweathertest.data.pojo.City
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list_city.*
import javax.inject.Inject

class CityListFragment : MvpAppCompatFragment(), Injectable, CityListView {

    @Inject
    @InjectPresenter
    lateinit var presenter: CityListPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter = CityListAdapter { presenter.onLoadWeather(it) }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
    }

    private fun initSearchView() {
        searchView.queryHint = getString(R.string.find_city)
        compositeDisposable.add(RxSearchView.queryTextChanges(searchView)
            .filter { query -> query.length > 1 || query.isEmpty() }
            .subscribe { it?.let { adapter.filterByName(it.toString()) } })
    }

    private fun initRecyclerView() {
        rvListCity.adapter = adapter
        rvListCity.layoutManager = LinearLayoutManager(context)
    }

    override fun showCities(it: List<City>) {
        adapter.setItems(it)
    }

    override fun showWeather(city: City) {
        adapter.setItem(city)
    }

    override fun showProgress(show: Boolean) {
        progressBar.visibility = when (show) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}
