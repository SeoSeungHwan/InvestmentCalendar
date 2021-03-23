package com.router.investmentcalendar.ui.chart

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.router.investmentcalendar.R
import kotlinx.android.synthetic.main.fragment_chart.view.*
import java.util.*
import kotlin.collections.ArrayList


class ChartFragment : Fragment() {


    val viewModel = ChartFragmentViewModel()
    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chart, container, false)


        val values: ArrayList<Entry> = ArrayList()
        viewModel.fetchWeekDate()
        viewModel.mutableLiveDataArrayList.observe(viewLifecycleOwner, Observer {
            var xIndex = 0.0f
            it.forEach {
                val dateArr = it.date.split("-")
                val calendar = Calendar.getInstance()
                calendar.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt())
                values.add(Entry(xIndex,
                    it.profit_asset.toFloat()))
                xIndex += 1
            }
            val set1 = LineDataSet(values, "DataSet 1")

            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets

            val data = LineData(dataSets)

            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.setDrawFilled(true)
            root.chart.setData(data)

            Log.d(TAG, "onCreateView: ss")

        })
        val set1 = LineDataSet(values, "DataSet 1")
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1) // add the data sets
        val data = LineData(dataSets)
        set1.color = Color.BLACK
        set1.setCircleColor(Color.BLACK)
        root.chart.setData(data)


        return root
    }
}



