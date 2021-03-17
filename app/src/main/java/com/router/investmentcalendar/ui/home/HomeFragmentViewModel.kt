package com.router.investmentcalendar.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.router.investmentcalendar.GlobalApplication

class HomeFragmentViewModel : ViewModel() {
    val db = Firebase.firestore

    val investCollection = MutableLiveData<QuerySnapshot>()


    fun fetchInvestCollection(){
        db.collection(GlobalApplication.UserId)
            .get()
            .addOnSuccessListener { result ->
                investCollection.value = result
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}