package com.router.investmentcalendar.ui.list

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.R
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = Firebase.firestore
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,
            false
        )
        val arraylist = ArrayList<InvestItem>()

        db.collection(GlobalApplication.UserId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var gson = Gson()
                    val investItem = gson.fromJson(document.data.toString(), InvestItem::class.java)
                    Log.d(TAG, "onCreateView: haha")
                    arraylist.add(investItem)
                }
                val adapter = InvestListRecyclerViewAdapter(arraylist)
                root.invest_item_rv.layoutManager = layoutManager
                root.invest_item_rv.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }



        return root
    }
}