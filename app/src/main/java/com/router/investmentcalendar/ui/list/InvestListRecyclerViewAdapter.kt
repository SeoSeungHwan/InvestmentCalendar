package com.router.investmentcalendar.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem


class InvestListRecyclerViewAdapter(private val dataSet: ArrayList<InvestItem>) :
    RecyclerView.Adapter<InvestListRecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val start_asset_tv: TextView
        val finish_asset_tv: TextView
        val profit_asset_tv: TextView
        val profit_percent_tv : TextView
        val item_date_tv : TextView


        init {
            start_asset_tv = view.findViewById(R.id.item_startasset_tv)
            finish_asset_tv = view.findViewById(R.id.item_finishasset_tv)
            profit_asset_tv = view.findViewById(R.id.item_profitasset_tv)
            profit_percent_tv = view.findViewById(R.id.item_profitpercent_tv)
            item_date_tv = view.findViewById(R.id.item_date_tv)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.invest_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.start_asset_tv.text = dataSet[position].start_asset.toString()+"원"
        viewHolder.finish_asset_tv.text = dataSet[position].finish_asset.toString()+"원"
        viewHolder.profit_asset_tv.text = dataSet[position].profit_asset.toString()+"원"
        viewHolder.profit_percent_tv.text = dataSet[position].profit_percent.toString()+"%"
        viewHolder.item_date_tv.text = dataSet[position].date
    }
    override fun getItemCount() = dataSet.size



}