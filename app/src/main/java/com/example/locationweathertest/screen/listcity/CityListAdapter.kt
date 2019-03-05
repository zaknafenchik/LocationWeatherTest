package com.example.locationweathertest.screen.listcity

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.locationweathertest.R
import com.example.locationweathertest.data.pojo.City
import kotlinx.android.synthetic.main.recycler_city_list_item.view.*

class CityListAdapter(val onLoadWeather: (city: City) -> Unit) : RecyclerView.Adapter<CityListAdapter.CityListVH>() {

    private val cityList = arrayListOf<City>()
    private val filteredList = arrayListOf<City>()
    private var filterString = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CityListVH {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_city_list_item, viewGroup, false)
        return CityListVH(view)
    }

    override fun getItemCount(): Int = filteredList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(vh: CityListVH, position: Int) {
        val city = filteredList[position]

        vh.itemView.tvNameCity.text = city.name
        vh.itemView.tvDistance.text = "${city.distance} km"

        if (city.weather != null) {
            vh.itemView.tvTemp.text = city.weather!!.temperature.toString()
            vh.itemView.progressBar.visibility = View.INVISIBLE
            vh.itemView.tvTemp.visibility = View.VISIBLE

        } else {
            vh.itemView.progressBar.visibility = View.VISIBLE
            vh.itemView.tvTemp.visibility = View.INVISIBLE
            onLoadWeather(city)
        }
    }

    fun setItems(list: List<City>) {
        cityList.clear()
        cityList.addAll(list)
        cityList.sortBy { it.distance }
        filterByName(filterString)
    }

    fun setItem(city: City) {
        for ((index, value) in cityList.withIndex()) {
            if (value.name == city.name) {
                cityList[index] = city
                notifyItemChanged(index)
                return
            }
        }
    }

    fun filterByName(query: String) {
        filterString = query
        filteredList.clear()
        filteredList.addAll(cityList.filter { it -> it.name.contains(filterString, true) })
        notifyDataSetChanged()
    }

    class CityListVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}