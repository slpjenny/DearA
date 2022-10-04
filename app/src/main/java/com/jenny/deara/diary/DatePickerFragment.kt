package com.jenny.deara.diary

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.DialogFragment
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentDatePickerBinding

class DatePickerFragment(var iMonth: Int, var iYear: Int) : DialogFragment() {

    private lateinit var binding: FragmentDatePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatePickerBinding.inflate(inflater, container, false)

        // 배경 제거하기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //엑스 버튼
        binding.dateCloseBtn.setOnClickListener {
            dismiss()
        }

        //날짜 기본값
        binding.year.text = iYear.toString()
        monthPicker(iMonth)

        //년도 -1 버튼
        binding.yearChange1.setOnClickListener {
            if (iYear > 2000) { // 최솟값 2000
                iYear -= 1
                binding.year.text = iYear.toString()
            }
        }

        //년도 +1 버튼
        binding.yearChange2.setOnClickListener {
            if (iYear < 2030){ // 최댓값 2000
                iYear += 1
                binding.year.text = iYear.toString()
            }
        }

        // 달 버튼 클릭 이벤트
        binding.month1.setOnClickListener {
            iMonth = 1
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)

        }
        binding.month2.setOnClickListener {
            iMonth = 2
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month3.setOnClickListener {
            iMonth = 3
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month4.setOnClickListener {
            iMonth = 4
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month5.setOnClickListener {
            iMonth = 5
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month6.setOnClickListener {
            iMonth = 6
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month7.setOnClickListener {
            iMonth = 7
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month8.setOnClickListener {
            iMonth = 8
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month9.setOnClickListener {
            iMonth = 9
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month10.setOnClickListener {
            iMonth = 10
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month11.setOnClickListener {
            iMonth = 11
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }
        binding.month12.setOnClickListener {
            iMonth = 12
            setBackgrounDefault()
            monthPicker(iMonth)
            goDiaryFragment(iMonth, iYear)
        }

        return binding.root
    }


    // 달 누르면 색 바꿔주는 함수
    private fun monthPicker( iMonth : Int){
        if (iMonth == 1){
            binding.month1.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month1.setTextColor(Color.WHITE)
        }else if(iMonth == 2){
            binding.month2.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month2.setTextColor(Color.WHITE)
        }else if(iMonth == 3) {
            binding.month3.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month3.setTextColor(Color.WHITE)
        }else if(iMonth == 4){
            binding.month4.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month4.setTextColor(Color.WHITE)
        }else if(iMonth == 5){
            binding.month5.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month5.setTextColor(Color.WHITE)
        }else if(iMonth == 6){
            binding.month6.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month6.setTextColor(Color.WHITE)
        }else if(iMonth == 7){
            binding.month7.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month7.setTextColor(Color.WHITE)
        }else if(iMonth == 8){
            binding.month8.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month8.setTextColor(Color.WHITE)
        }else if(iMonth == 9){
            binding.month9.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month9.setTextColor(Color.WHITE)
        }else if(iMonth == 10){
            binding.month10.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month10.setTextColor(Color.WHITE)
        }else if(iMonth == 11){
            binding.month11.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month11.setTextColor(Color.WHITE)
        }else {
            binding.month12.background = context?.let { getDrawable(it, R.drawable.diary_button) }
            binding.month12.setTextColor(Color.WHITE)
        }
    }

    //기본값으로 변경해주는 함수
    @SuppressLint("ResourceType")
    private fun setBackgrounDefault(){
        binding.month1.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month2.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month3.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month4.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month5.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month6.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month7.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month8.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month9.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month10.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month11.setBackgroundColor(Color.parseColor("#00ff0000"))
        binding.month12.setBackgroundColor(Color.parseColor("#00ff0000"))

        binding.month1.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month2.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month3.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month4.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month5.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month6.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month7.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month8.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month9.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month10.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month11.setTextColor(Color.parseColor("#6A6A6A"))
        binding.month12.setTextColor(Color.parseColor("#6A6A6A"))
    }

    private fun goDiaryFragment(iMonth: Int, iYear: Int){
        // 다이어리 리스트로 이동
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("iMonth", iMonth)
        intent.putExtra("iYear", iYear)
        intent.putExtra("nav_diary","fourth")
        startActivity(intent)
    }
}

