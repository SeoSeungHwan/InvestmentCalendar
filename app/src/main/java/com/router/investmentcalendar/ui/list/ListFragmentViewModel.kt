package com.router.investmentcalendar.ui.list

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class ListFragmentViewModel :ViewModel() {

    val db = Firebase.firestore
    val mutableLiveDataArrayList = MutableLiveData<ArrayList<InvestItem>>()
    val arraylist = ArrayList<InvestItem>()
    val gson = Gson()

    init {
        sortDate()
    }

    fun sortDate(){
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
    fun sortProfitAsset(){
        Collections.sort(arraylist)
        mutableLiveDataArrayList.value = arraylist
    }

}