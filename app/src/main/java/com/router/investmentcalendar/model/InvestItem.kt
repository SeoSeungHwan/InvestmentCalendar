package com.router.investmentcalendar.model

data class InvestItem(
    var date : String,
    var start_asset: Long,
    var finish_asset: Long,
    var profit_asset: Long,
    var profit_percent: Double
) :Comparable<InvestItem> {
    constructor() :this("",0,0,0,0.0)

    override fun compareTo(investItem: InvestItem): Int {
        return this.profit_asset.compareTo(investItem.profit_asset)
    }
}
