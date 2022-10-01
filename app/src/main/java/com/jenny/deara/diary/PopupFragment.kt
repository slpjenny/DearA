package com.jenny.deara.diary

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jenny.deara.DiaryFragment
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentPopupBinding

class PopupFragment : DialogFragment() {

    private lateinit var binding:FragmentPopupBinding
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopupBinding.inflate(inflater, container, false)

        // 배경 제거하기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //취소 버튼
        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }

        //삭제버튼
        binding.delBtn.setOnClickListener{
            // 삭제 버튼 누른 뒤 이벤트 구현

            // 다이어리 리스트로 이동
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("nav_diary", "fourth")
            startActivity(intent)
        }
        return binding.root
    }

}