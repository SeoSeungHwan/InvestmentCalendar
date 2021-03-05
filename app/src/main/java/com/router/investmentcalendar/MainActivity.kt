package com.router.investmentcalendar


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.util.*


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var investItem = InvestItem("1000000","true")

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("user")
        myRef.child("userid").child(LocalDate.now().toString()).setValue(investItem)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                dataSnapshot.children.forEach(){
                    Log.w("Test", it.value.toString())
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Test", "error")
            }
        }
        myRef.addValueEventListener(postListener)
        updateKaKaoLoginUi()


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("Test", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("Test", "로그인 성공 ${token.accessToken}")
                updateKaKaoLoginUi()

            }
        }

        login_btn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        }

        logout_btn.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("Test", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i("Test", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    updateKaKaoLoginUi()
                }
            }
        }
    }

    fun updateKaKaoLoginUi() {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                name_tv.text = user.id.toString()
                Glide.with(profile_iv).load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .circleCrop().into(profile_iv)

                login_btn.visibility = View.GONE
                logout_btn.visibility = View.VISIBLE


            } else {
                name_tv.text = null
                profile_iv.setImageBitmap(null)
                login_btn.visibility = View.VISIBLE
                logout_btn.visibility = View.GONE
            }
            return@me
        }
    }


}