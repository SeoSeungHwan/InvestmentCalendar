package com.router.investmentcalendar.ui.home


import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.CalendarUtils
import com.applandeo.materialcalendarview.EventDay
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    val viewModel = HomeFragmentViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val gson = Gson()

        //캘린더 수익률 표시
        viewModel.fetchInvestCollection()

        //CalendarView 클릭시 해당 날짜 투자내역 show
        root.calendarView.setOnDayClickListener { eventDay ->
            val user = db.collection(GlobalApplication.UserId).document(getSelectDate(eventDay.calendar))
            user.get().addOnSuccessListener {
                date_tv.text = getSelectDate(eventDay.calendar)

                val investItem = gson.fromJson(it.data.toString(), InvestItem::class.java)
                var decimalFormat = DecimalFormat("###,###")
                if (investItem != null) {
                    root.remove_investmemo_btn.visibility = View.VISIBLE
                    start_asset_tv.text = decimalFormat.format(investItem.start_asset).toString()
                    finish_asset_tv.text = decimalFormat.format(investItem.finish_asset).toString()

                    if(investItem.profit_percent>=0){
                        profit_asset_tv.text = "+"+decimalFormat.format(investItem.profit_asset).toString()
                        profit_percent_tv.text = "+"+investItem.profit_percent.toString()
                        profit_asset_tv.setTextColor(Color.parseColor("#4CAF50"))
                        profit_percent_tv.setTextColor(Color.parseColor("#4CAF50"))
                    }else {
                        profit_asset_tv.text = decimalFormat.format(investItem.profit_asset).toString()
                        profit_percent_tv.text = investItem.profit_percent.toString()
                        profit_asset_tv.setTextColor(Color.parseColor("#F44336"))
                        profit_percent_tv.setTextColor(Color.parseColor("#F44336"))
                    }
                } else {
                    root.remove_investmemo_btn.visibility = View.INVISIBLE
                    start_asset_tv.text = null
                    finish_asset_tv.text = null
                    profit_asset_tv.text = null
                    profit_percent_tv.text = null

                }
            }
        }

        //자산추가 floating버튼 클릭 이벤트
        root.add_investmemo_btn.setOnClickListener {
            try {
                val action = HomeFragmentDirections.actionNavigationHomeToAddInvestItemFragment(
                    getSelectDate(
                        calendarView.firstSelectedDate
                    )
                )
                findNavController().navigate(action)
            }catch (e : Exception){
                Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }

        }

        //TODO 투자내역삭제해도 바로 달력에서 사라지지 않음
        //자산삭제 floating버튼 클릭 이벤트
        root.remove_investmemo_btn.setOnClickListener {
            db.collection(GlobalApplication.UserId).document(getSelectDate(calendarView.firstSelectedDate))
                .delete()
                .addOnSuccessListener {
                    viewModel.fetchInvestCollection()
                    Toast.makeText(context, "투자내역이 삭제되었습니다.", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

        }
        return root
    }


    fun getSelectDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}-${month}-${day}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //TODO Viewmodel에서 activity 얻어오기 구현
        val gson = Gson()
        val events = ArrayList<EventDay>()
        viewModel.investCollection.observe(viewLifecycleOwner, androidx.lifecycle.Observer {result->
            for (document in result) {
                Log.d(GlobalApplication.UserId, "${document.id} => ${document.data}")
                val investItem = gson.fromJson(document.data.toString(), InvestItem::class.java)
                val profit_text : Drawable
                if(investItem.profit_percent>=0){
                    profit_text = CalendarUtils.getDrawableText(
                        activity, investItem.profit_percent.toInt().toString()+"%", null, android.R.color.holo_green_light, 9
                    )
                }else{
                    profit_text= CalendarUtils.getDrawableText(
                        activity, investItem.profit_percent.toInt().toString()+"%", null, android.R.color.holo_red_light, 9
                    )
                }

                var dateArr = document.id.split("-")
                val calendar = Calendar.getInstance()
                calendar.set(dateArr[0].toInt(),dateArr[1].toInt()-1,dateArr[2].toInt())
                events.add(EventDay(calendar,profit_text))
                calendarView.setEvents(events)
            }
        })
    }

}