package com.router.investmentcalendar

import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        user_profile_iv.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("로그아웃")
            builder.setMessage("로그아웃 하시겠습니까?")
            builder.setPositiveButton("예"){dialog,which->
                UserApiClient.instance.logout {
                    if(it != null){
                        Toast.makeText(this,"로그아웃 실패",Toast.LENGTH_SHORT).show()
                    }else {
                        val intent = Intent(this, LoginAcitivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        updateKaKaoLoginUi()
                    }
                }
            }
            builder.setNegativeButton("아니요"){dialog,which->

            }
            builder.show()
        }

    }

    fun updateKaKaoLoginUi() {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                user_name_tv.text = user.kakaoAccount?.profile?.nickname + " 님"
                Glide.with(user_profile_iv).load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .circleCrop().into(user_profile_iv)
            } else {
                user_name_tv.text = null
                user_profile_iv.setImageBitmap(null)
            }
            return@me
        }
    }

    var mBackWait:Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }
}