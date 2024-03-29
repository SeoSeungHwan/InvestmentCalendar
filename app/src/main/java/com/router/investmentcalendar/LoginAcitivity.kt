package com.router.investmentcalendar

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login_acitivity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginAcitivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_acitivity)


            try {
                val packageInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNING_CERTIFICATES
                )
                val signingInfo = packageInfo.signingInfo.apkContentsSigners

                for (signature in signingInfo) {
                    val messageDigest = MessageDigest.getInstance("SHA")
                    messageDigest.update(signature.toByteArray())
                    val keyHash = String(Base64.encode(messageDigest.digest(), 0))
                    Log.d("KeyHash", keyHash)
                }

            } catch (e: Exception) {
                Log.e("Exception", e.toString())
            }


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("Test", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("Test", "로그인 성공 ${token.accessToken}")

                val intent = Intent(this,MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }

        //TODO AMD와 실제기기 사이의 로그인 오류 해결하기
        login_btn.setOnClickListener {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginAcitivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginAcitivity, callback = callback)
                    updateKaKaoUserId() //UserId 초기화
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginAcitivity, callback = callback)
                    updateKaKaoUserId() //UserId 초기화
                }
        }

    }


    fun updateKaKaoUserId() {
        UserApiClient.instance.me { user, error ->
            if (user != null) {
             GlobalApplication.UserId = user.id.toString()
                Log.d("TEST", "updateKaKaoUserId: "+ user.id.toString())
            } else {

            }
            return@me
        }
    }
}