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
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.android.synthetic.main.fragment_chart.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class ChartFragment : Fragment() {

    val viewModel = ChartFragmentViewModel()

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
            val set1 = LineDataSet(values, "일일 수익률 변화")

            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets

            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.setDrawFilled(true)
            set1.setDrawCircleHole(true)

            val xAxis = root.chart.xAxis
            xAxis.textColor = Color.BLACK
            xAxis.setDrawGridLines(false)

            val yAxis = root.chart.axisRight
            yAxis.setDrawGridLines(false)

            val data = LineData(dataSets)
            root.chart.setData(data)
            root.chart.description.text = ""

            root.chart.invalidate()
        })


        //데이터 가져올때까지 Progressbar 나타나게하기
        viewModel.loadingLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {

                progressBar2.visibility = View.VISIBLE
            } else {
                progressBar2.visibility = View.GONE
            }
        })

        return root
    }
}



