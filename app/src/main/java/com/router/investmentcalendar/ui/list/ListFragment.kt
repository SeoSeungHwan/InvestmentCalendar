package com.router.investmentcalendar.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.router.investmentcalendar.R
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    val viewModel: ListFragmentViewModel by viewModels()
    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        root = inflater.inflate(R.layout.fragment_list, container, false)

        root?.sort_profitasset_rb?.setOnClickListener {
            viewModel.sortProfitAsset()
        }
        root?.sort_date_rb?.setOnClickListener {
            viewModel.sortDate()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val linearLayoutMangerWrapper = LinearLayoutMangerWrapper(context!!,
            RecyclerView.VERTICAL,
            false
        )
        viewModel.mutableLiveDataArrayList.observe(viewLifecycleOwner, Observer {
            val adapter = InvestListRecyclerViewAdapter(it)
            root?.invest_item_rv?.layoutManager = linearLayoutMangerWrapper
            root?.invest_item_rv?.adapter = adapter
        })
    }
}