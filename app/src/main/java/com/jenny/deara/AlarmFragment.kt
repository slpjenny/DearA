package com.jenny.deara

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jenny.deara.alarm.AlarmDialog
import com.jenny.deara.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm, container, false)

        binding.addAlarmBtn.setOnClickListener {
            val dialog = AlarmDialog()
            dialog.show(parentFragmentManager, "CustomDialog")
        }



        return binding.root
    }


}