package com.router.investmentcalendar.ui.home

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.kakao.sdk.user.UserApiClient
import com.router.investmentcalendar.AddInvestItemFragment
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.activity_login_acitivity.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment() {


    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("user")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //사용자 이미지와 이름 추가
        updateKaKaoLoginUi()


        //TODO 해당아이디 수익률 쫙불러와 달력에 수익률 표시하기

        //TODO 달력날짜 클릭시 해당 날짜 시작금액 마감금액 수익률 표시하기
        root.calendarView.setOnDayClickListener { eventDay ->
          /*  myRef.child(GlobalApplication.UserId).child(getSelectDate(eventDay.calendar)).get()
                .addOnSuccessListener { dataSnapShot ->
                    var investItem = dataSnapShot.getValue(InvestItem::class.java)
                    if (investItem != null) {
                        asset_tv.text = investItem.asset
                    } else {
                        asset_tv.text =null
                    }

                    //Log.d(TAG, "onCreateView: ${dataSnapShot.value}")
                }.addOnFailureListener {
                Log.d(TAG, "onCreateView: "+it.message)
            }*/
        }

        //TODO safeargs로 날짜 전달
        //자산추가 floating버튼 클릭 이벤트
        root.add_investmemo_btn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_addInvestItemFragment)
        }
        return root
    }


    fun getSelectDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}.${month}.${day}"
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