package com.jenny.deara

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.databinding.FragmentHomeBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val TAG = HomeFragment::class.java.simpleName

    // 년월 변수
    lateinit var selecteDate : LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // 현재 날짜
        selecteDate = LocalDate.now()

        // 화면 설정
        setMonthView()

        // 이전달 버튼 이벤트
        binding.preBtn.setOnClickListener {
            // 현재 월 -1 변수 담기
            selecteDate = selecteDate.minusMonths(1)
            setMonthView()
        }

        // 다음달 버튼 이벤트
        binding.nextBtn.setOnClickListener {
            selecteDate = selecteDate.plusMonths(1)
            setMonthView()
        }

        return binding.root
    }

    // 날짜 화면에 보여주기
    private fun setMonthView() {
        // 년월 테스트뷰 세팅
        binding.dateText.text = monthYearFromData(selecteDate)

        // 날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(selecteDate)

        // 어댑터 초기화
        val adapter = CalendarAdapter(dayList)

        // 레이아웃 설정 (열 7개)
        var manager : RecyclerView.LayoutManager = GridLayoutManager(context,7)

        // 레이아웃 적용
        binding.calendarRv.layoutManager = manager

        // 어댑터 적용
        binding.calendarRv.adapter = adapter
    }

    // 날짜 타입 설정
    private fun monthYearFromData(date : LocalDate) : String{

        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")

        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }

    // 날짜 생성
    private fun dayInMonthArray(date: LocalDate) : ArrayList<String>{

        var dayList = ArrayList<String>()

        var yearMonth = YearMonth.from(date)

        // 해당 월 마지막 날짜 가져오기 (예: 28, 30, 31)
        var lastDay = yearMonth.lengthOfMonth()

        // 해당 월의 첫 번째 날 가져오기 (예: 4월 1일)
        var firstDay = selecteDate.withDayOfMonth(1)

        // 첫 번째날 요일 가져오기 (월:1, 일:7)
        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..41){
            if(i <= dayOfWeek || i>(lastDay + dayOfWeek)) {
                dayList.add("")
            }
            else {
                dayList.add((i - dayOfWeek).toString())
            }

        }

        if (dayList[6] == ""){
            for(i in 1..7) {
                dayList.removeAt(0)
            }
        }

        Log.d(TAG, "데이리스트 : " + dayList.size.toString())
        Log.d(TAG, "데이리스트 : " + dayList)
        Log.d(TAG, "firstDay : " + firstDay.toString())
        Log.d(TAG, "lastDay : " + lastDay.toString())
        Log.d(TAG, "dayOfWeek : " + dayOfWeek.toString())

        return dayList
    }



}