package com.jenny.deara

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.alarm.AlarmData
import com.jenny.deara.alarm.AlarmDialog
import com.jenny.deara.alarm.AlarmListAdapter
import com.jenny.deara.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding

    lateinit var AlarmListAdapter: AlarmListAdapter
    val datas = mutableListOf<AlarmData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm, container, false)

        initRecycler()

        // 마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 알람 추가 버튼
        binding.addAlarmBtn.setOnClickListener {
            val dialog = AlarmDialog()
            dialog.show(parentFragmentManager, "CustomDialog")
        }

        return binding.root
    }

    private fun initRecycler() {
        AlarmListAdapter = AlarmListAdapter(requireContext())

        val rv : RecyclerView = binding.rvAlarm
        rv.adapter = AlarmListAdapter

        // test 데이터
        datas.apply{
            add(AlarmData("13:45", "점심 약", "매일"))
            add(AlarmData("19:30", "저녁 약", "주말"))

            AlarmListAdapter.datas = datas
            AlarmListAdapter.notifyDataSetChanged()
        }
    }


}