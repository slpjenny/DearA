package com.jenny.deara

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.alarm.AlarmData
import com.jenny.deara.alarm.AlarmDialog
import com.jenny.deara.alarm.AlarmListAdapter
import com.jenny.deara.databinding.FragmentAlarmBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.alarm_dialog.*

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding

    lateinit var AlarmListAdapter: AlarmListAdapter

    // val datas = mutableListOf<AlarmData>()
    val alarmList = mutableListOf<AlarmData>()
    val alarmkeyList = mutableListOf<String>()

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
        getFBAlarmData()

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
                    val uid = FBAuth.getUid()
                    val key = FBRef.alarmRef.push().key.toString()

                    if (hour.toInt() < 10 && minute.toInt() < 10) {
                        time = "0" + hour.toString() + ":0" + minute.toString()
                    } else if(hour.toInt() < 10 && minute.toInt () >= 10) {
                        time = "0" + hour.toString() + ":" + minute.toString()
                    } else if (hour.toInt() > 10 && minute.toInt () < 10) {
                        time = hour.toString() + ":0" + minute.toString()
                    } else {
                        time = hour.toString() + ":" + minute.toString()
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

                    FBRef.alarmRef
                        .child(key)
                        .setValue(AlarmData(time, title, day, uid))


                   /* datas.apply{
                        add(AlarmData(time, title, day))

                        AlarmListAdapter.datas = datas
                        AlarmListAdapter.notifyDataSetChanged()
                    }*/


                }

            })
        }


        // 수정

        AlarmListAdapter.setDataClickListener(object: AlarmListAdapter.DataClickListener{
            override fun onClick(view: View, position: Int) {

                val dialog = AlarmDialog()
                val key = alarmkeyList[position]
                dialog.show(parentFragmentManager, "CustomDialog")


                // 이전 데이터 띄우기
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataModel = dataSnapshot.getValue(AlarmData::class.java)

                        if (dataModel != null) {
                            dialog.setAlarmName.setText(dataModel.title)
                            dialog.timePicker.hour = dataModel.time.substring(0 until 2).toInt()
                            dialog.timePicker.minute = dataModel.time.substring(3 until 5).toInt()

                            if(dataModel.day.contains("월")) {
                                dialog.monCheck.isChecked = true
                            }

                            if(dataModel.day.contains("화")) {
                                dialog.tueCheck.isChecked = true
                            }

                            if(dataModel.day.contains("수")) {
                                dialog.wedCheck.isChecked = true
                            }

                            if(dataModel.day.contains("목")) {
                                dialog.thuCheck.isChecked = true
                            }

                            if(dataModel.day.contains("금")) {
                                dialog.friCheck.isChecked = true
                            }

                            if(dataModel.day.contains("토")) {
                                dialog.satCheck.isChecked = true
                            }

                            if(dataModel.day.contains("일")) {
                                dialog.sunCheck.isChecked = true
                            }

                            if(dataModel.day == "주말") {
                                dialog.satCheck.isChecked = true
                                dialog.sunCheck.isChecked = true
                            }

                            if(dataModel.day == "매일") {
                                dialog.monCheck.isChecked = true
                                dialog.tueCheck.isChecked = true
                                dialog.wedCheck.isChecked = true
                                dialog.thuCheck.isChecked = true
                                dialog.friCheck.isChecked = true
                                dialog.satCheck.isChecked = true
                                dialog.sunCheck.isChecked = true
                            }

                            if(dataModel.day == "주중") {
                                dialog.monCheck.isChecked = true
                                dialog.tueCheck.isChecked = true
                                dialog.wedCheck.isChecked = true
                                dialog.thuCheck.isChecked = true
                                dialog.friCheck.isChecked = true
                            }

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("alarmTest", "loadPost:onCancelled", databaseError.toException())
                    }
                }

                FBRef.alarmRef.child(key).addValueEventListener(postListener)


                /*// 삭제 하기
                rmAlarm.setOnClickListener {
                    FBRef.alarmRef.child(key).removeValue()
                    AlarmListAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }*/


                // 다시 수정하기
                dialog.setOnClickedListener(object: AlarmDialog.ButtonClickListener {

                    override fun onClicked(hour: String, minute: String,  title: String, day: String) {


                        var hour: String = hour
                        var minute: String = minute
                        var time: String
                        var title: String = title
                        var day: String = day
                        val uid = FBAuth.getUid()

                        /*if(minute.toInt() < 10) {
                            time= hour.toString() +":0"+ minute.toString()
                        } else {
                            time= hour.toString() +":"+ minute.toString()
                        }*/

                        if (hour.toInt() < 10 && minute.toInt() < 10) {
                            time = "0" + hour.toString() + ":0" + minute.toString()
                        } else if(hour.toInt() < 10 && minute.toInt () >= 10) {
                            time = "0" + hour.toString() + ":" + minute.toString()
                        } else if (hour.toInt() > 10 && minute.toInt () < 10) {
                            time = hour.toString() + ":0" + minute.toString()
                        } else {
                            time = hour.toString() + ":" + minute.toString()
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
                        FBRef.alarmRef
                            .child(key)
                            .setValue(AlarmData(time, title, day, uid))

                    }

                })
            }
        })

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        AlarmListAdapter = AlarmListAdapter(requireContext(), alarmkeyList)

        val rv : RecyclerView = binding.rvAlarm
        rv.adapter= AlarmListAdapter

        AlarmListAdapter.datas = alarmList
        AlarmListAdapter.notifyDataSetChanged()
    }

    //파이어베이스 데이터 불러오기
    private fun getFBAlarmData(){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                alarmList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("alarmListTest", alarmList.toString())

                    val item = dataModel.getValue(AlarmData::class.java)
                    if(FBAuth.getUid() == item!!.uid){
                        alarmList.add(item)
                        alarmkeyList.add(dataModel.key.toString())
                    }



                }
                alarmkeyList.reverse()
                alarmList.reverse()
                AlarmListAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("alarmListTest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.alarmRef.addValueEventListener(postListener)
    }

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

