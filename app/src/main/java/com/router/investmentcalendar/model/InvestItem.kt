package com.router.investmentcalendar.model

data class InvestItem(
    var start_asset: Long,
    var finish_asset: Long,
    var profit_asset: Long,
    var profit_percent: Double
){
    constructor() :this(0,0,0,0.0)
}
