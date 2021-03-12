package com.router.investmentcalendar.ui.home


import android.content.ContentValues.TAG
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
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val gson = Gson()


        val events = ArrayList<EventDay>()



        db.collection(GlobalApplication.UserId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(GlobalApplication.UserId, "${document.id} => ${document.data}")
                    val investItem = gson.fromJson(document.data.toString(), InvestItem::class.java)
                    val profit_text : Drawable
                    if(investItem.profit_percent>=0){
                        profit_text = CalendarUtils.getDrawableText(
                            activity, "+", null, android.R.color.holo_green_light, 30
                        )
                    }else{
                        profit_text= CalendarUtils.getDrawableText(
                            activity, "-", null, android.R.color.holo_red_light, 30
                        )
                    }

                    var dateArr = document.id.split("-")
                    val calendar = Calendar.getInstance()
                    calendar.set(dateArr[0].toInt(),dateArr[1].toInt()-1,dateArr[2].toInt())
                    events.add(EventDay(calendar,profit_text))
                    calendarView.setEvents(events)

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        root.calendarView.setOnDayClickListener { eventDay ->
            val user = db.collection(GlobalApplication.UserId).document(getSelectDate(eventDay.calendar))
            user.get().addOnSuccessListener {
                date_tv.text = getSelectDate(eventDay.calendar)

                val investItem = gson.fromJson(it.data.toString(), InvestItem::class.java)
                if (investItem != null) {
                    start_asset_tv.text = investItem.start_asset.toString()
                    finish_asset_tv.text = investItem.finish_asset.toString()
                    profit_asset_tv.text = investItem.profit_asset.toString()
                    profit_percent_tv.text = investItem.profit_percent.toString()
                } else {
                    start_asset_tv.text = null
                    finish_asset_tv.text = null
                    profit_asset_tv.text = null


                    profit_percent_tv.text = null
                    Toast.makeText(context, "투자내역을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
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
        return root
    }


    fun getSelectDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}-${month}-${day}"
    }


}