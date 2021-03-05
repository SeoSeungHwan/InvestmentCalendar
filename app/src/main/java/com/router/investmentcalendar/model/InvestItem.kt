package com.router.investmentcalendar.model

data class InvestItem(
    var asset: String?,
    var profit: String?
){
    constructor() :this("","")
}
