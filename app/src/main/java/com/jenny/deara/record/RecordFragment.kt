package com.jenny.deara.record

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentDiaryBinding
import com.jenny.deara.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        // 진료기록 추가 화면으로 페이지 전환
        binding.plusButton.setOnClickListener {
            val intent = Intent(context,AddRecordActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

}