package com.router.investmentcalendar.ui.list

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem
import java.text.DecimalFormat


class InvestListRecyclerViewAdapter(private val dataSet: ArrayList<InvestItem>) :
    RecyclerView.Adapter<InvestListRecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val start_asset_tv: TextView
        val finish_asset_tv: TextView
        val profit_asset_tv: TextView
        val profit_percent_tv : TextView
        val item_date_tv : TextView
        val item_linearlayout : LinearLayout


        init {
            start_asset_tv = view.findViewById(R.id.item_startasset_tv)
            finish_asset_tv = view.findViewById(R.id.item_finishasset_tv)
            profit_asset_tv = view.findViewById(R.id.item_profitasset_tv)
            profit_percent_tv = view.findViewById(R.id.item_profitpercent_tv)
            item_date_tv = view.findViewById(R.id.item_date_tv)
            item_linearlayout = view.findViewById(R.id.item_linearlayout)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.invest_item, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        var decimalFormat = DecimalFormat("###,###")
        viewHolder.start_asset_tv.text = decimalFormat.format(dataSet[position].start_asset).toString()+"원"
        viewHolder.finish_asset_tv.text = decimalFormat.format(dataSet[position].finish_asset).toString()+"원"
        if(dataSet[position].profit_percent>=0){
            viewHolder.item_linearlayout.setBackgroundColor(Color.parseColor("#4CAF50"))
            viewHolder.profit_asset_tv.text = "+"+decimalFormat.format(dataSet[position].profit_asset).toString()+"원"
            viewHolder.profit_percent_tv.text = "+"+dataSet[position].profit_percent.toString()+"%"
        }else{
            viewHolder.item_linearlayout.setBackgroundColor(Color.parseColor("#F44336"))
            viewHolder.profit_asset_tv.text = decimalFormat.format(dataSet[position].profit_asset).toString()+"원"
            viewHolder.profit_percent_tv.text = dataSet[position].profit_percent.toString()+"%"
        }

        viewHolder.item_date_tv.text = dataSet[position].date


    }
    override fun getItemCount() = dataSet.size



}