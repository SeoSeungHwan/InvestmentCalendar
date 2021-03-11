package com.router.investmentcalendar.ui.list

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.router.investmentcalendar.GlobalApplication
import com.router.investmentcalendar.R

class ListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,
            false
        )

/*        val adapter = InvestListRecyclerViewAdapter(list)
        group_list_rv.layoutManager = layoutManager
        group_list_rv.adapter = adapter*/

        return root
    }
}