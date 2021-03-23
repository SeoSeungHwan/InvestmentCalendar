package com.router.investmentcalendar


import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_add_invest_item.*
import kotlinx.android.synthetic.main.fragment_add_invest_item.view.*

class AddInvestItemFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add_invest_item, container, false)
        val args: AddInvestItemFragmentArgs by navArgs()

        val db = Firebase.firestore
        root.start_asset_tv.text = args.date + " 시작금액"
        root.finish_asset_tv.text = args.date + " 마감금액"


        root.add_group_frame_btn.setOnClickListener {
            val result = hashMapOf(
                "start_asset" to start_asset_et.text.toString().toLong(),
                "finish_asset" to finish_asset_et.text.toString().toLong(),
                "profit_asset" to profit_asset_tv.text.toString().toLong(),
                "profit_percent" to profit_percent_tv.text.toString().toDouble()
            )
            db.collection(GlobalApplication.UserId).document(args.date).set(result)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.action_addInvestItemFragment_to_navigation_home)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "기록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
        }


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val start_esset: Long? = root.start_asset_et.text.toString().toLongOrNull()
                val finish_esset: Long? = root.finish_asset_et.text.toString().toLongOrNull()
                if ((start_esset != null) and (finish_esset != null) and (start_esset != 0L)) {
                    var profit_asset = finish_esset!! - start_esset!!
                    var profit_percent =
                        (Math.round(((finish_esset.toDouble() / start_esset) - 1) * 1000) / 1000.0) * 100
                    Log.d(TAG, "onTextChanged: "+finish_esset.toDouble() / start_esset)
                    root.profit_asset_tv.text = profit_asset.toString()
                    root.profit_percent_tv.text = profit_percent.toString()
                }
            }
        }
        root.start_asset_et.addTextChangedListener(textWatcher)
        root.finish_asset_et.addTextChangedListener(textWatcher)

        return root
    }


}