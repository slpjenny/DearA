package com.jenny.deara.diary

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.DatePickerBindingAdapter
import androidx.fragment.app.DialogFragment
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentDatePickerBinding

class DatePickerFragment : DialogFragment() {

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

        return binding.root
    }

}