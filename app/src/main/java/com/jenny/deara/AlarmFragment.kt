package com.jenny.deara

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.alarm.*
import com.jenny.deara.databinding.AlarmDialogBinding
import com.jenny.deara.databinding.FragmentAlarmBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.alarm_dialog.*
import java.security.SecureRandom
import java.util.*

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private lateinit var _binding: AlarmDialogBinding

    lateinit var AlarmListAdapter: AlarmListAdapter

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.alarm_dialog, container, false)
        getFBAlarmData()
        initRecycler()
        updateOnOff()
        // startAlarm()
        // var OnOff = updateOnOff()

        var alarmIdList = addAlarmID()
        val secureRandom = SecureRandom()
        var alarmId: Int = secureRandom.nextInt()


        // 마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 알람 추가 버튼
        binding.addAlarmBtn.setOnClickListener {
            var uid = FBAuth.getUid()
            var key = FBRef.alarmRef.push().key.toString()


            val dialog = AlarmDialog()
            dialog.show(parentFragmentManager, "CustomDialog")
            dialog.setOnClickedListener(object: AlarmDialog.ButtonClickListener {
                override fun onClicked(hour: String, minute: String,  title: String, day: String) {

                    var hour: String = hour
                    var minute: String = minute
                    var time: String
                    var title: String = title
                    var day: String = day

                    var i : Int = 0

                    while(i < alarmIdList.size) {
                        if (alarmId == alarmIdList[i]) {
                            alarmId = secureRandom.nextInt()
                        }
                        i++
                    }

                    if (hour.toInt() < 10 && minute.toInt() < 10) {
                        time = "0" + hour.toString() + ":0" + minute.toString()
                    } else if(hour.toInt() < 10 && minute.toInt () >= 10) {
                        time = "0" + hour.toString() + ":" + minute.toString()
                    } else if (hour.toInt() >= 10 && minute.toInt () < 10) {
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
                        .setValue(AlarmData(time, title, day, uid, alarmId, true))

                    // sendOnChannel1(title)
                    startAlarm(time, alarmId, title, day)

                }

            })

        }


        // 수정

        AlarmListAdapter.setDataClickListener(object: AlarmListAdapter.DataClickListener{
            override fun onClick(view: View, position: Int) {

                val dialog = AlarmDialog()
                val key = alarmkeyList[position]
                var alarmID = 0
                dialog.show(parentFragmentManager, "CustomDialog")

                // 이전 데이터 띄우기
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataModel = dataSnapshot.getValue(AlarmData::class.java)

                        if (dataModel != null) {

                            // val rmAlarm = dialog.findViewById<Button>(R.id.rmAlarm)
                            // 삭제 기능
                            dialog.rmAlarm.setOnClickListener {
                                alarmID = dataModel.alarmId
                                removeAlarm(alarmID)
                                FBRef.alarmRef.child(key).removeValue()
                                AlarmListAdapter.notifyDataSetChanged()
                                dialog.dismiss()
                            }

                            // 이전 데이터 부르기

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

                // 다시 수정하기
                dialog.setOnClickedListener(object: AlarmDialog.ButtonClickListener {

                    override fun onClicked(hour: String, minute: String,  title: String, day: String) {

                        var hour: String = hour
                        var minute: String = minute
                        var time: String
                        var title: String = title
                        var day: String = day
                        val uid = FBAuth.getUid()

                        if (hour.toInt() < 10 && minute.toInt() < 10) {
                            time = "0" + hour.toString() + ":0" + minute.toString()
                        } else if(hour.toInt() < 10 && minute.toInt () >= 10) {
                            time = "0" + hour.toString() + ":" + minute.toString()
                        } else if (hour.toInt() >= 10 && minute.toInt () < 10) {
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
                            .setValue(AlarmData(time, title, day, uid, alarmID, true))

                        removeAlarm(alarmId)
                        startAlarm(time, alarmId, title, day)
                    }

                })
            }
        })

        return binding.root
    }

/*    private fun sendOnChannel1(title: String) {
        var notificationHelper: NotificationHelper = NotificationHelper(context)

        var nb: NotificationCompat.Builder = notificationHelper.getChannelNotification(title)

        //알림 호출
        notificationHelper.getManager().notify(1, nb.build())
    }*/

    private fun updateOnOff() {

        AlarmListAdapter.setOnClickedListener(object: AlarmListAdapter.SwitchClickListener {
            override fun onClicked(position: Int, OnOff: Boolean) {

                val key = alarmkeyList[position]
                FBRef.alarmRef.child(key).child("onOff").setValue(OnOff)

                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val dataModel = dataSnapshot.getValue(AlarmData::class.java)

                        if (dataModel != null) {
                            if(dataModel.onOff){
                                startAlarm(dataModel.time, dataModel.alarmId, dataModel.title, dataModel.day)
                            } else {
                                removeAlarm(dataModel.alarmId)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w("alarmTest", "loadPost:onCancelled", databaseError.toException())
                    }
                }
                FBRef.alarmRef.child(key).addValueEventListener(postListener)
            }
        })
    }

    private fun addAlarmID(): ArrayList<Int> {
        var alarmIdList = ArrayList<Int>()

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(AlarmData::class.java)
                    if(FBAuth.getUid() == item!!.uid && item != null) {
                        alarmIdList.add(item.alarmId)
                        Log.d("size", alarmIdList.size.toString())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("alarmListTest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.alarmRef.addValueEventListener(postListener)

        return alarmIdList
    }

    private fun startAlarm(time: String, alarmId: Int, title: String, day: String) {
        requireActivity()?.runOnUiThread {

            var alarmManager: AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            var intent = Intent(requireActivity(), AlertReceiver::class.java)
            intent.putExtra("title", title)


            var week: BooleanArray = BooleanArray(7) {
                false
            }

            if (day == "매일") {
                week[0] = true
                week[1] = true
                week[2] = true
                week[3] = true
                week[4] = true
                week[5] = true
                week[6] = true
            }
            if (day == "주중") {
                week[1] = true
                week[2] = true
                week[3] = true
                week[4] = true
                week[5] = true
            }
            if (day == "주말") {
                week[0] = true
                week[6] = true
            }
            if (day.contains("일")){
                week[0] = true
            }
            if (day.contains("월")){
                week[1] = true
            }
            if (day.contains("화")){
                week[2] = true
            }
            if (day.contains("수")){
                week[3] = true
            }
            if (day.contains("목")){
                week[4] = true
            }
            if (day.contains("금")){
                week[5] = true
            }
            if (day.contains("토")){
                week[6] = true
            }

            intent.putExtra("week", week)


            var pendingIntent = PendingIntent.getBroadcast(requireActivity(), alarmId, intent,
                FLAG_MUTABLE or FLAG_UPDATE_CURRENT)

            /*var sTime = System.currentTimeMillis()
            var cTime = calendar.timeInMillis

            val interval = 1000 * 60 * 60 *24 // 하루 뒤

            //설정 시간이 현재시간 이전이면 +1일
            while (sTime > cTime) {
                cTime += interval
            }*/

            var calendar = Calendar.getInstance()
            calendar.setTimeInMillis(System.currentTimeMillis())
            // calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            calendar.set(Calendar.HOUR_OF_DAY, time.substring(0 until 2).toInt())
            calendar.set(Calendar.MINUTE, time.substring(3 until 5).toInt())
            calendar.set(Calendar.SECOND, 0)



            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }


            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,AlarmManager.INTERVAL_DAY * 1, pendingIntent)
        }
    }


    private fun removeAlarm(alarmId: Int) {
        var alarmManager: AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(requireActivity(), AlertReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(requireActivity(), alarmId, intent,
            FLAG_MUTABLE or FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
        
        /*requireActivity().runOnUiThread {
            var intent = Intent(requireActivity(), AlertReceiver::class.java)
            var pendingIntent = PendingIntent.getBroadcast(requireActivity(), alarmId, intent,
                FLAG_MUTABLE or FLAG_UPDATE_CURRENT)

            alarmManager.cancel(pendingIntent)
        }*/
        Log.d("editAlarm", "remove")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        AlarmListAdapter = AlarmListAdapter(requireContext(), alarmkeyList)

        val rv : RecyclerView = binding.rvAlarm
        rv.adapter= AlarmListAdapter

        AlarmListAdapter.datas = alarmList
        AlarmListAdapter.notifyDataSetChanged()
    }

    // 파이어베이스 데이터 불러오기
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