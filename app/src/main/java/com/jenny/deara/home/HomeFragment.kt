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
import com.jenny.deara.diary.DiaryListAdapter
import com.jenny.deara.utils.CalendarUtil
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.calendar_item.*
import kotlinx.android.synthetic.main.fragment_diary.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    lateinit var TodoAdapter: TodoAdapter

    val items = ArrayList<ToDoData>()
    val todokeyList = mutableListOf<String>()

    val TAG = HomeFragment::class.java.simpleName

    var dateCalendar = Calendar.getInstance()

    var year = (dateCalendar.get(Calendar.YEAR)).toString()
    var month =(dateCalendar.get(Calendar.MONTH) + 1).toString()
    var day = (dateCalendar.get(Calendar.DAY_OF_MONTH)).toString()
    var day2 = (dateCalendar.get(Calendar.DAY_OF_MONTH)).toString()

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
        setMonthView()

        initRecycler()
        getFBFirstTodoData()

        // 이전달 버튼 이벤트
        binding.preBtn.setOnClickListener {
            // 현재 월 -1 변수 담기
            CalendarUtil.selectedDate.add(Calendar.MONTH, -1) // 현재 달 -1
            setMonthView()
        }

        // 다음달 버튼 이벤트
        binding.nextBtn.setOnClickListener {

            CalendarUtil.selectedDate.add(Calendar.MONTH , 1) // 현재 달 +1
            setMonthView()
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

        TodoAdapter = TodoAdapter(requireContext(), items, todokeyList)
        val rv: RecyclerView = binding.toDoListRV
        rv.adapter = TodoAdapter

        TodoAdapter.notifyDataSetChanged()
    }


    // 파이어베이스에 데이터 저장
    fun saveTodo(year : String, month: String, day : String, text: String){


        var todo : String = text
        var check : Boolean = false
        val time = FBAuth.getTimeDiary()
        val uid = FBAuth.getUid()

        // 할일 목록 추가
        items.add(ToDoData(todo, check, time, year, month, day, uid))
        Log.d(TAG, "saveTodo : " + items)

        val key = FBRef.todoRef.push().key.toString()
        Log.d(TAG, "keyadd : " + key)

        FBRef.todoRef
            .child(key)
            .setValue(ToDoData(todo, check, time, year, month, day, uid))
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
                    // 파이어베이스에 투두 항목 저장하기
                    saveTodo(year, month, day2, text)
                    val rv: RecyclerView = binding.toDoListRV
                    val rvRvAdapter = TodoAdapter(requireContext(), items, todokeyList)

                    rv.adapter = rvRvAdapter
                    rv.layoutManager = LinearLayoutManager(context)
                    rvRvAdapter.notifyDataSetChanged()

                }

            }
        })
    }


//  화면 시작할 시 해당 날짜 투두리스트 firebase에서 불러오기
    fun getFBFirstTodoData(){

        val position = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()

                for (dataModel in dataSnapshot.children){
                    Log.d("todoList", dataModel.toString())

                    var firstMontn = (CalendarUtil.selectedDate.get(Calendar.MONTH)+1).toString()
                    var firstday = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH).toString()
                    Log.d(TAG, "firstMont :" + firstMontn)
                    Log.d(TAG, "firstday : " + firstday)
                    val item = dataModel.getValue(ToDoData::class.java)
                    if (FBAuth.getUid() == item!!.uid){
                        if(item!!.month == firstMontn && item!!.date == firstday ){
                            items.add(item)
                            todokeyList.add(dataModel.key.toString())
                        }
                    }
                    item
                    todokeyList
                    TodoAdapter.notifyDataSetChanged()

                    Log.d(TAG, "todoList : " + items)

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("getTodoFB", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.todoRef.addValueEventListener(position)
    }

    // firebase에 저장된 투두리스트 목록 불러오기
    fun getFBTodoData(Month : String, date : String){

        val position = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()

                for (dataModel in dataSnapshot.children){
                    Log.d("todoList", dataModel.toString())

                    val item = dataModel.getValue(ToDoData::class.java)
                    if (FBAuth.getUid() == item!!.uid){
                        if(item!!.month == Month && item!!.date == date ){
                            items.add(item)
                            todokeyList.add(dataModel.key.toString())
                            Log.d (TAG, "monthhh : " + item!!.month)
                            Log.d (TAG, "monthhhh : " + Month)
                            Log.d (TAG, "datehhh : " + item!!.date)
                            Log.d (TAG, "datehhhh : " + date)
                        }
                    }
                    items.reverse()
                    todokeyList.reverse()
                    TodoAdapter.notifyDataSetChanged()

                    Log.d(TAG, "todoList : " + items)

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("getTodoFB", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.todoRef.addValueEventListener(position)
    }


    // 날짜 화면에 보여주기
    private fun setMonthView() {
        // 년월 테스트뷰 세팅
        binding.dateText.text = monthYearFromData(CalendarUtil.selectedDate)

        // 날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray()

        // 어댑터 초기화
        val adapter = CalendarAdapter(requireContext(), dayList, items, todokeyList)

        // 레이아웃 설정 (열 7개)
        var manager : RecyclerView.LayoutManager = GridLayoutManager(context,7)

        // 레이아웃 적용
        binding.calendarRv.layoutManager = manager

        // 어댑터 적용
        binding.calendarRv.adapter = adapter

        adapter.itemClick = object : CalendarAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                var dateCalendar = Calendar.getInstance()

                year = CalendarUtil.selectedDate.get(Calendar.YEAR).toString()
                month = (CalendarUtil.selectedDate.get(Calendar.MONTH)+1).toString()
                day = dayList[position].toString()
                day2 = (day[8].toString() + day[9].toString())

                Log.d(TAG, "itemclick year : " + year)
                Log.d(TAG, "itemclick month : " + month)
                Log.d(TAG, "itemclick day : " + day2)


                initRecycler()
                getFBTodoData(month,day2)
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

        Log.d(TAG, "monthCalendar2 : " + monthCalendar)

        // 해당 달의 1일의 요일 [1:일요일, 2:월요일... 7일:토요일]
        val firstDayofMonth = monthCalendar[Calendar.DAY_OF_WEEK]-1
        Log.d(TAG, "monthCalendar3 : " + (monthCalendar[Calendar.DAY_OF_WEEK]-1))

        // 요일 숫자만큼 이전 날짜로 변경
        // 예 : 6월 1일이 수요일이면 3만큼 이전날짜 셋팅
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth)

        while(dayList.size<42){

            dayList.add(monthCalendar.time)

            // 1일씩 늘린다. 1일 -> 2일 -> 3일
            monthCalendar.add(Calendar.DAY_OF_MONTH,1 )
        }


        Log.d(TAG, "데이리스트 : " + dayList.size.toString())
        Log.d(TAG, "데이리스트 : " + dayList)

        return dayList
    }



}