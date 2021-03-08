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
import kotlinx.android.synthetic.main.fragment_add_invest_item.*

class AddInvestItemFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        add_group_frame_btn.setOnClickListener {
            findNavController().navigate(R.id.action_addInvestItemFragment_to_navigation_home)
        }

        return inflater.inflate(R.layout.fragment_add_invest_item, container, false)
    }



}