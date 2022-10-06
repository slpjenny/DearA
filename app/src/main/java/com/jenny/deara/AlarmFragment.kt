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
import kotlinx.coroutines.NonDisposableHandle.parent


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

        // initRecycler()

        AlarmListAdapter = AlarmListAdapter(requireContext())

        val rv : RecyclerView = binding.rvAlarm
        rv.adapter = AlarmListAdapter

        // 마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 알람 추가 버튼
        binding.addAlarmBtn.setOnClickListener {
            val dialog = AlarmDialog()
            dialog.show(parentFragmentManager, "CustomDialog")

            dialog.setOnClickedListener(object: AlarmDialog.ButtonClickListener {
                override fun onClicked(hour: String, minute: String,  title: String, day: String) {
                    var hour: String = hour
                    var minute: String = minute
                    var time: String
                    var title: String = title
                    var day: String = day

                    if(minute.toInt() < 10) {
                        time= hour.toString() +":0"+ minute.toString()
                    } else {
                        time= hour.toString() +":"+ minute.toString()
                    }

                    if (day.replace(" ", "") == "월화수목금토일") {
                        day = "매일"
                    } else if (day.replace(" ", "") == "토일") {
                        day = "주말"
                    } else if (day.replace(" ", "") == "월화수목금") {
                        day = "주중"
                    } else {
                        day = day
                    }


                    datas.apply{
                        add(AlarmData(time, title, day))

                        AlarmListAdapter.datas = datas
                        AlarmListAdapter.notifyDataSetChanged()
                    }


                }

            })
        }

        binding.rvAlarm.setOnClickListener{
            val dialog = AlarmDialog()
            dialog.show(parentFragmentManager, "CustomDialog")

            dialog.setOnClickedListener(object: AlarmDialog.ButtonClickListener {
                override fun onClicked(hour: String, minute: String,  title: String, day: String) {
                    var hour: String = hour
                    var minute: String = minute
                    var time: String
                    var title: String = title
                    var day: String = day

                    if(minute.toInt() < 10) {
                        time= hour.toString() +":0"+ minute.toString()
                    } else {
                        time= hour.toString() +":"+ minute.toString()
                    }

                    if (day.replace(" ", "") == "월화수목금토일") {
                        day = "매일"
                    } else if (day.replace(" ", "") == "토일") {
                        day = "주말"
                    } else if (day.replace(" ", "") == "월화수목금") {
                        day = "주중"
                    } else {
                        day = day
                    }


                    datas.apply{
                        add(AlarmData(time, title, day))

                        AlarmListAdapter.datas = datas
                        AlarmListAdapter.notifyDataSetChanged()
                    }


                }

            })
        }

        return binding.root
    }

/*    private fun initRecycler() {
        AlarmListAdapter = AlarmListAdapter(requireContext())

        val rv : RecyclerView = binding.rvAlarm
        rv.adapter = AlarmListAdapter

        // test 데이터
        datas.apply{
            add(AlarmData("13:00", "점심 약", "매일"))
            add(AlarmData("19:30", "저녁 약", "주말"))

            AlarmListAdapter.datas = datas
            AlarmListAdapter.notifyDataSetChanged()
        }
    }*/
}