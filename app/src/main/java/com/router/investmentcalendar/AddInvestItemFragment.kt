package com.router.investmentcalendar

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.router.investmentcalendar.model.InvestItem
import kotlinx.android.synthetic.main.fragment_add_invest_item.*
import kotlinx.android.synthetic.main.fragment_add_invest_item.view.*

class AddInvestItemFragment : Fragment() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("user")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add_invest_item, container, false)

        root.add_group_frame_btn.setOnClickListener {


            //TODO 수익률 가격 , 퍼센트 계산해서 setValue 에 집어넣기
            myRef.child(GlobalApplication.UserId).child("2021-03-08")
                .setValue(InvestItem(start_asset_et.text.toString().toLong(),
                    finish_asset_et.text.toString().toLong(),
                    profit_asset_tv.text.toString().toLong(),
                    profit_percent_tv.text.toString().toDouble()))

            //TODO 수익률 가격, 퍼센트 TextView에 나타나게 하기
            findNavController().navigate(R.id.action_addInvestItemFragment_to_navigation_home)
        }

        return root
    }



}