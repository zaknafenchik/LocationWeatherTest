package com.example.locationweathertest.screen.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.example.locationweathertest.screen.listcity.CityListFragment
import com.example.locationweathertest.screen.map.MapFragment
import java.util.*

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val tabs = ArrayList<String>()
    private val registeredFragments = SparseArray<Fragment>()

    init {
        tabs.add("List")
        tabs.add("Map")
    }

    override fun getItem(position: Int): Fragment? {
        if (position == 0) {
            val listCityFragment = CityListFragment()
            registeredFragments.put(position, listCityFragment)
            return listCityFragment
        }
        if (position == 1) {
            val mapFragment = MapFragment()
            registeredFragments.put(position, mapFragment)
            return mapFragment
        }
        return null
    }


    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
}
