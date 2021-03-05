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
        root.calendarView.setOnDayClickListener {
            //Log.d(TAG, "onCreateView: " + it.calendar.toString())
        }

        //TODO 해당되는 아이디에대한 InvestItem목록 가져오기
        myRef.child(GlobalApplication.UserId).get().addOnSuccessListener {
            Log.d(TAG, "onCreateView: "+it.value)
        }

        //달력 Day클릭 시 그날 자산현황 나타내기
        root.calendarView.setOnDayClickListener { eventDay ->
            myRef.child(GlobalApplication.UserId).child(getSelectDate(eventDay.calendar)).get()
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
            }


        }
        //자산추가 floating버튼 클릭 이벤트
        root.add_investmemo_btn.setOnClickListener {
            addInvestItem_Dialog(getSelectDate(calendarView.firstSelectedDate))
        }
        return root
    }


    fun getSelectDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}-${month}-${day}"
    }

    //자산 추가시 파이어베이스에 입력하는 함수
    //TODO 자산 입력 시 전날대비 수익인지 손해인지
    @RequiresApi(Build.VERSION_CODES.O)
    fun addInvestItem_Dialog(selectDate: String) {

        val editText = EditText(this.context)
        editText.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED + InputType.TYPE_CLASS_NUMBER
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(selectDate)
        builder.setMessage("금액을 입력해주세요.")
        builder.setView(editText)
        builder.setPositiveButton("입력", DialogInterface.OnClickListener { dialogInterface, i ->
            myRef.child(GlobalApplication.UserId).child(selectDate)
                .setValue(InvestItem(editText.text.toString(), "true"))
            Toast.makeText(this.context, "자산이 입력되었습니다.", Toast.LENGTH_SHORT).show()
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->

        })
        builder.show()
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