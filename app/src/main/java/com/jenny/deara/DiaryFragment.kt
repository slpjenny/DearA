package com.jenny.deara

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.databinding.FragmentDiaryBinding
import com.jenny.deara.diary.*

class DiaryFragment(var iMonth: Int, var iYear: Int) : Fragment() {

    private lateinit var binding: FragmentDiaryBinding

    lateinit var DiaryListAdapter: DiaryListAdapter
    val datas = mutableListOf<DiaryData>()
    val stringMonth : List<String> = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
    val intYear = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)

        for (i in 2000 .. 2030){
            intYear.add(i)
        }

        initRecycler()
        showMonth(iMonth)
        showYear(iYear)

        //마이페이지 버튼
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
        }

        //일기 작성하기
        binding.diaryplusBtn.setOnClickListener{
            val intent = Intent(context, DiaryWriteActivity::class.java)
            startActivity(intent)
        }

        //날짜 선택하기
        binding.datapicker.setOnClickListener {
            showDatePickerDialog(iMonth, iYear)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        DiaryListAdapter = DiaryListAdapter(requireContext())

        val rv : RecyclerView = binding.rvDiary
        rv.adapter= DiaryListAdapter

        //test 데이터
        datas.apply {
            add(DiaryData(
                "좋았던일",
                "오늘은 맛있는 음식을 많이 먹어서 좋았다.",
                "안 좋았던 일",
                "늦잠을 자서 지각할 뻔",
                "나의 다짐",
                "내일은 꼭 일찍 일어나기",
                "[랜덤질문] 가장 좋았던 여행은 어떤 여행인가요?",
                "작년 여름에 가족들과 갔던 부산여행")
            )
            add(DiaryData(
                "좋았던일",
                "일찍 일어났다",
                "안 좋았던 일",
                "늦게 자서 피곤해",
                "나의 다짐",
                "내일은 꼭 일찍 자기",
                "[랜덤질문] 가장 좋았던 여행은 어떤 여행인가요?",
                "작년 여름에 가족들과 갔던 부산여행")
            )
            DiaryListAdapter.datas = datas
            DiaryListAdapter.notifyDataSetChanged()

        }
    }

    private fun showDatePickerDialog(iMonth: Int, iYear: Int){
        val dialog = DatePickerFragment(iMonth, iYear)
        dialog.show(childFragmentManager, "DatePickerDialog")
    }

    //달 띄우기
    private fun showMonth(iMonth: Int){
        for (i in 1 .. 12){
            if (iMonth == i){
                binding.month.text = stringMonth[i-1]
            }
        }
    }

    private fun showYear(iYear: Int){
        for (i in 0 .. 30){
            if (iYear == i+2000){
                binding.year.text = intYear[i].toString()
            }
        }
    }
}