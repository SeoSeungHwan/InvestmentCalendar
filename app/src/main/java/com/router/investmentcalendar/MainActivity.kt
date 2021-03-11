package com.router.investmentcalendar

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        //사용자 이미지와 이름 추가
        updateKaKaoLoginUi()


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