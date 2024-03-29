package com.router.investmentcalendar.ui.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.applandeo.materialcalendarview.CalendarUtils
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.router.investmentcalendar.GlobalApplication

class HomeFragmentViewModel : ViewModel() {
    val db = Firebase.firestore
    val investCollection = MutableLiveData<QuerySnapshot>()

    val loadingLiveData = MutableLiveData<Boolean>()
    fun fetchInvestCollection(){
        loadingLiveData.value = true
        Log.d(TAG, "fetchInvestCollection: "+ GlobalApplication.UserId)
        db.collection(GlobalApplication.UserId)
            .get()
            .addOnSuccessListener { result ->
                investCollection.value = result
                loadingLiveData.postValue(false)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}