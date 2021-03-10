package com.router.investmentcalendar.ui.home


import android.content.ContentValues.TAG
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
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.kakao.sdk.user.UserApiClient
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.activity_login_acitivity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment() {


    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference(GlobalApplication.UserId)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //사용자 이미지와 이름 추가
        updateKaKaoLoginUi()


        //TODO : 투자내역을 불러와 달력에 수익률 표시
        myRef.get().addOnSuccessListener {
                Log.d(TAG, "onCreateView: " + it.value)

        }


        root.calendarView.setOnDayClickListener { eventDay ->
            myRef.child(getSelectDate(eventDay.calendar)).get()
                .addOnSuccessListener { dataSnapShot ->
                    var investItem = dataSnapShot.getValue(InvestItem::class.java)
                    if (investItem != null) {
                        date_tv.text = getSelectDate(eventDay.calendar)
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

                    //Log.d(TAG, "onCreateView: ${dataSnapShot.value}")
                }.addOnFailureListener {
                    Log.d(TAG, "onCreateView: " + it.message)
            }
        }

        //자산추가 floating버튼 클릭 이벤트
        root.add_investmemo_btn.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAddInvestItemFragment(
                getSelectDate(
                    calendarView.firstSelectedDate
                )
            )
            findNavController().navigate(action)
        }
        return root
    }


    fun getSelectDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}-${month}-${day}"
    }

    fun updateKaKaoLoginUi() {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                user_name_tv.text = user.kakaoAccount?.profile?.nickname + "님 환영합니다."
                Glide.with(user_profile_iv).load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .circleCrop().into(user_profile_iv)
            } else {
                user_name_tv.text = null
                user_profile_iv.setImageBitmap(null)
            }
            return@me
        }
    }

}