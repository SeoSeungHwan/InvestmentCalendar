package com.router.investmentcalendar

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
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
                .setValue(
                    InvestItem(
                        start_asset_et.text.toString().toLong(),
                        finish_asset_et.text.toString().toLong(),
                        profit_asset_tv.text.toString().toLong(),
                        profit_percent_tv.text.toString().toDouble()
                    )
                )

            findNavController().navigate(R.id.action_addInvestItemFragment_to_navigation_home)
        }

        //평가손액 textchangeevent
        var profit_asset =0L
        var profit_percent =0.0
         val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val start_esset :Long? =root.start_asset_et.text.toString().toLongOrNull()
                val finish_esset :Long? = root.finish_asset_et.text.toString().toLongOrNull()
                if((start_esset!=null) and (finish_esset!= null) and (start_esset!=0L)){
                     profit_asset= finish_esset!! - start_esset!!
                     profit_percent = (Math.round(((finish_esset.toDouble() /start_esset) - 1) * 100)/100.0) *100
                    root.profit_asset_tv.text = profit_asset.toString()+"원"
                    root.profit_percent_tv.text = profit_percent.toString()+"%"
                }
            }
        }
        root.start_asset_et.addTextChangedListener(textWatcher)
        root.finish_asset_et.addTextChangedListener(textWatcher)

        return root
    }


}