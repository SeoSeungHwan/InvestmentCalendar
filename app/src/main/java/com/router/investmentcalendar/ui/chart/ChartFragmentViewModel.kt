package com.router.investmentcalendar.ui.chart

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.model.InvestItem

class ChartFragmentViewModel : ViewModel() {

    val db = Firebase.firestore
    val gson = Gson()

    val arraylist = ArrayList<InvestItem>()
    val mutableLiveDataArrayList = MutableLiveData<ArrayList<InvestItem>>()

    fun fetchWeekDate(){
        arraylist.clear()
        db.collection(GlobalApplication.UserId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val investItem = gson.fromJson(document.data.toString(), InvestItem::class.java)
                    investItem.date = document.id
                    arraylist.add(investItem)
                }
                mutableLiveDataArrayList.value = arraylist
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


}