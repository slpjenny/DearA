package com.jenny.deara

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.databinding.FragmentHomeBinding
import com.jenny.deara.mypages.ToDoModel
import com.jenny.deara.mypages.TodoAdapter
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val items = ArrayList<ToDoModel>()

    private val TAG = HomeFragment::class.java.simpleName

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
            val dialog = TodoDialog(requireContext())
            dialog.showDia()
            dialog.setOnClickListener(object: TodoDialog.ButtonClickListener {
                override fun onClicked(text: String) {

                    if (text.length == 0){
                        Toast.makeText(context, "해아할 일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }else{
                        items.add(ToDoModel(text,false))
                        Log.d(TAG, "items " + items.size)
                        Log.d(TAG, "text : " + text)

                        val rv : RecyclerView = binding.toDoListRV
                        val rvRvAdapter = TodoAdapter(items)

                        rv.adapter = rvRvAdapter
                        rv.layoutManager = LinearLayoutManager(context)
                        rvRvAdapter.notifyDataSetChanged()
                    }

                }
            })



        }

       return binding.root
    }

    // 날짜 화면에 보여주기
    private fun setMonthView() {
        // 년월 테스트뷰 세팅
        binding.dateText.text = monthYearFromData(CalendarUtil.selectedDate)

        // 날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray()

        // 어댑터 초기화
        val adapter = CalendarAdapter(dayList)

        // 레이아웃 설정 (열 7개)
        var manager : RecyclerView.LayoutManager = GridLayoutManager(context,7)

        // 레이아웃 적용
        binding.calendarRv.layoutManager = manager

        // 어댑터 적용
        binding.calendarRv.adapter = adapter

        adapter.itemClick = object : CalendarAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(context, dayList[position].toString(), Toast.LENGTH_LONG).show()
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


//        var yearMonth = YearMonth.from(date)
//
//        // 해당 월 마지막 날짜 가져오기 (예: 28, 30, 31)
//        var lastDay = yearMonth.lengthOfMonth()
//
//        // 해당 월의 첫 번째 날 가져오기 (예: 4월 1일)
//        var firstDay = CalendarUtil.selectedDate.withDayOfMonth(1)
//
//        // 첫 번째날 요일 가져오기 (월:1, 일:7)
//        var dayOfWeek = firstDay.dayOfWeek.value
//
//        for(i in 1..41){
//            if(i <= dayOfWeek || i>(lastDay + dayOfWeek)) {
//                dayList.add(null)
//            }
//            else {
//                dayList.add(LocalDate.of(CalendarUtil.selectedDate.year,
//                    CalendarUtil.selectedDate.monthValue, i - dayOfWeek))
//            }
//
//        }
//
//        if (dayList[6] == null){
//            for(i in 1..7) {
//                dayList.removeAt(0)
//            }
//        }

        Log.d(TAG, "데이리스트 : " + dayList.size.toString())
        Log.d(TAG, "데이리스트 : " + dayList)
//        Log.d(TAG, "firstDay : " + firstDay.toString())
//        Log.d(TAG, "lastDay : " + lastDay.toString())
//        Log.d(TAG, "dayOfWeek : " + dayOfWeek.toString())

        return dayList
    }



}