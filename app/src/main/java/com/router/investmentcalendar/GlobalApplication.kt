package com.router.investmentcalendar

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.router.investmentcalendar.model.InvestItem

class GlobalApplication : Application() {

    companion object{
        var UserId = ""
        var hashMap = HashMap<String,InvestItem>()
    }
    override fun onCreate() {
        super.onCreate()

        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "ded44abc1984738ef8f6fe5bdbc67aba")
    }

}