package com.jenny.deara.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.alarm.AlarmData
import com.jenny.deara.alarm.AlarmListAdapter
import com.jenny.deara.databinding.FragmentHomeBinding
import com.jenny.deara.diary.DiaryDetailActivity
import com.jenny.deara.diary.DiaryListAdapter
import com.jenny.deara.record.RecordData
import com.jenny.deara.record.TodayRecordData
import com.jenny.deara.utils.CalendarUtil
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.calendar_item.*
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_diary.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    lateinit var TodoAdapter: TodoAdapter

    // 투두 Recyclerview 관련 변수
    val items = ArrayList<ToDoData>()
    val todokeyList = mutableListOf<String>()

    // 오늘 진료기록 Recyclerview 관련 변수
    lateinit var MainTodayRcAdapter: MainTodayRcAdapter

    val records = mutableListOf<MainTodayRecordData>()
    val recordKeyList = mutableListOf<String>()

    val TAG = HomeFragment::class.java.simpleName

    var dateCalendar = Calendar.getInstance()

    var year = (dateCalendar.get(Calendar.YEAR)).toString()
    var month =(dateCalendar.get(Calendar.MONTH) + 1).toString()
    var day = (dateCalendar.get(Calendar.DAY_OF_MONTH)).toString()
    var day2 = (dateCalendar.get(Calendar.DAY_OF_MONTH)).toString()

    var firstYear = (CalendarUtil.selectedDate.get(Calendar.YEAR).toString())
    var firstMonth = (CalendarUtil.selectedDate.get(Calendar.MONTH)+1).toString()
    var firstday = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH).toString()


    var percent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // 화면 설정
        setMonthView(percent)

        initRecycler()

        // 오늘 진료기록 띄우기
        initTodayRecycler()
        getFBTodayRcData()

        // 날짜 길이가 1인 경우 앞에 0 붙여주기
        if (firstday.length == 1){
            firstday = "0"+firstday
        }
        getFBTodoData(firstYear,firstMonth, firstday)
        todokeyList.distinct()

        Log.d(TAG, "percent 1 : " + percent)

        binding.dateText2.text = month + "월 " + day2 + "일"


        // 이전달 버튼 이벤트
        binding.preBtn.setOnClickListener {
            // 현재 월 -1 변수 담기
            CalendarUtil.selectedDate.add(Calendar.MONTH, -1) // 현재 달 -1
            setMonthView(percent)
        }

        // 다음달 버튼 이벤트
        binding.nextBtn.setOnClickListener {

            CalendarUtil.selectedDate.add(Calendar.MONTH , 1) // 현재 달 +1
            setMonthView(percent)
        }

        // 투두리스트 추가
        binding.todoplus.setOnClickListener {
            todoAdd()
        }

        // 마이페이지로 이동
        binding.mypageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)

        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(){

        TodoAdapter = TodoAdapter(requireContext(), items, year, month, day2)
        val rv: RecyclerView = binding.toDoListRV
        rv.adapter = TodoAdapter

        TodoAdapter.notifyDataSetChanged()
    }

    // 오늘 진료기록 RecyclerView 초기화
    private fun initTodayRecycler(){

        MainTodayRcAdapter = MainTodayRcAdapter(requireContext(), recordKeyList)
        val todayRv: RecyclerView = binding.todayRcRvMain
        todayRv.adapter = MainTodayRcAdapter

        MainTodayRcAdapter.datas = records
        MainTodayRcAdapter.notifyDataSetChanged()
    }


    // 파이어베이스에 데이터 저장
    fun saveTodo(year : String, month: String, day : String, text: String){

        var todo : String = text
        var check : Boolean = false
        val time = FBAuth.getTimeDiary()
        val uid = FBAuth.getUid()

        val key = FBRef.todoRef.push().key.toString()

        // 할일 목록 추가
        items.add(ToDoData(todo, check, time, year, month, day, uid, key, percent))
        Log.d(TAG, "saveTodo : " + items)

        FBRef.todoRef
            .child(uid)
            .child(year)
            .child(month)
            .child(day)
            .child(key)
            .setValue(ToDoData(todo, check, time, year, month, day, uid, key, percent))
    }


    // 투두리스트 다이얼로그로 추가
    fun todoAdd(){

        val dialog = TodoDialog(requireContext())

        dialog.showDia()

        dialog.setOnClickListener(object: TodoDialog.ButtonClickListener {
            override fun onClicked(text: String) {


                if (text.length == 0){
                    Toast.makeText(context, "해아할 일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }else {

                    if (day2.length == 1){
                        day2 = "0"+day2
                    }
                    // 파이어베이스에 투두 항목 저장하기
                    saveTodo(year, month, day2, text)
                    val rv: RecyclerView = binding.toDoListRV
                    val rvRvAdapter = TodoAdapter(requireContext(), items, year, month, day2)

                    rv.adapter = rvRvAdapter
                    rv.layoutManager = LinearLayoutManager(context)
                    rvRvAdapter.notifyDataSetChanged()

                }

            }
        })
    }

    // firebase에서 오늘 진료 기록 불러오기
    private fun getFBTodayRcData(){

        // 오늘 날짜 불러오기
        var now = LocalDate.now()
        var nowDate :String = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 "))
        var nowDay :String = now.dayOfMonth.toString()

        // 시스템에서 불러오는 오늘 날짜는 두자리수로 표현되기에 한자리수로 맞추기위해 따로 처리해줌
        nowDate = nowDate+nowDay+"일"

        val postListener = object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                records.clear()

                for (dataModel in dataSnapshot.children) {
                    // 리싸이클러뷰 데이터에 항목 세개만 넣어서 추가하기
                    val item = dataModel.getValue(RecordData::class.java)
                    val todayItem = dataModel.getValue(MainTodayRecordData::class.java)

                    if (item != null) {
                        // uid 에 맞는 진료기록들을 불러오기
                        if(FBAuth.getUid() == item.uid){

                            // 날짜가 같은게 있다면, 따로 불러와서 todayRcRv 에도!! 추가해야함
                            if(item.date == nowDate){
                                if (todayItem != null) {
                                    records.add(todayItem)
                                    recordKeyList.add(dataModel.key.toString())

                                    Log.d("today", todayItem.toString())
                                    Log.d("today11", item.toString())

                                }
                            }

                        }
                    }

                }

                MainTodayRcAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("recordListTest", "취소되었습니다.", databaseError.toException())
            }
        }
        FBRef.recordRef.addValueEventListener(postListener)

    }


    // firebase에 저장된 투두리스트 목록 불러오기
    fun getFBTodoData(Year : String, Month : String, date : String){

        val position = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                TodoAdapter.notifyDataSetChanged()

                items.clear()

                var count = 0
                var myuid = FBAuth.getUid()
                if (day2.length == 1){
                    day2 = "0"+day2
                }

                val test = dataSnapshot.child(myuid).child(Year).child(Month).child(date)

                for (dataModel in test.children){
                    Log.d("todoList", dataModel.toString())

                    val item = dataModel.getValue(ToDoData::class.java)
                    Log.d(TAG,"item.uid : " + item!!.uid)

                            items.add(item)
                            if (todokeyList.none { it == dataModel.key }) {
                                todokeyList.add(dataModel.key.toString())
                            }
                            Log.d(TAG, "todokeyList : " + todokeyList)
                            Log.d(TAG, "todokeyList : " + todokeyList)

                            // 체크 된 할일 목록에 따라 프로그레스바 설정
                            if (item!!.check == true) {
                                count++
                            }

                            percent = count * 100 / items.size
                            //progressBar.progress = item.percent
                            binding.progressBar.progress = item.percent

                            setMonthView(percent)

                            FBRef.todoRef
                                .child(item.uid)
                                .child(item.year)
                                .child(item.month)
                                .child(item.date)
                                .child(item.key)
                                .child("percent")
                                .setValue(percent)

                    TodoAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("getTodoFB", "loadPost:onCancelled", databaseError.toException())
            }
        }

        FBRef.todoRef.addValueEventListener(position)
    }


    // 날짜 화면에 보여주기
    private fun setMonthView(percent : Int) {
        // 년월 테스트뷰 세팅
        binding.dateText.text = monthYearFromData(CalendarUtil.selectedDate)

        // 날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray()

        // 어댑터 초기화
        val adapter = context?.let { CalendarAdapter(it, dayList, items, todokeyList, percent) }

        // 레이아웃 설정 (열 7개)
        var manager : RecyclerView.LayoutManager = GridLayoutManager(context,7)

        // 레이아웃 적용
        binding.calendarRv.layoutManager = manager

        // 어댑터 적용
        binding.calendarRv.adapter = adapter

        // 날짜 선택할 시
        adapter?.itemClick = object : CalendarAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

                // 날짜 생성해서 리스트에 담기
                val dayList = dayInMonthArray()
                year = CalendarUtil.selectedDate.get(Calendar.YEAR).toString()
                month = (CalendarUtil.selectedDate.get(Calendar.MONTH)+1).toString()
                day = dayList[position].toString()
                day2 = (day[8].toString() + day[9].toString())

                firstMonth = month
                firstday = day2

                binding.dateText2.text = month + "월 " + day2 + "일"

                progressBar.progress = 0

                initRecycler()
                getFBTodoData(year,month,day2)
                TodoAdapter.notifyDataSetChanged()

            }
        }
    }

    // 날짜 타입 설정
    private fun monthYearFromData(calendar: Calendar) : String{

        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH) + 1

        return "$year"+"년"+" "+"$month"+"월"
    }

    // 날짜 생성
    private fun dayInMonthArray() : ArrayList<Date>{

        var dayList = ArrayList<Date>()

        var monthCalendar = CalendarUtil.selectedDate.clone() as Calendar

        Log.d(TAG, "monthCalendar : " + monthCalendar)

        // 1일로 세팅
        monthCalendar[Calendar.DAY_OF_MONTH] = 1

        // 해당 달의 1일의 요일 [1:일요일, 2:월요일... 7일:토요일]
        val firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1

        // 요일 숫자만큼 이전 날짜로 변경
        // 예 : 6월 1일이 수요일이면 3만큼 이전날짜 셋팅
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth)

        while(dayList.size<42){

            dayList.add(monthCalendar.time)

            // 1일씩 늘린다. 1일 -> 2일 -> 3일
            monthCalendar.add(Calendar.DAY_OF_MONTH,1 )
        }

        return dayList
    }



}