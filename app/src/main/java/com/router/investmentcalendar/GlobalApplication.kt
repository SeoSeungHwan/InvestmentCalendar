package com.router.investmentcalendar

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.KakaoSdk
import com.router.investmentcalendar.model.InvestItem

class GlobalApplication : Application() {

    companion object{
        var UserId = ""
    }
    override fun onCreate() {
        super.onCreate()

        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "ded44abc1984738ef8f6fe5bdbc67aba")
    }

}