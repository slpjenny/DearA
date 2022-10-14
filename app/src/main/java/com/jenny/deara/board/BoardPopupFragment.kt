package com.jenny.deara.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentBoardPopupBinding
import com.jenny.deara.databinding.FragmentDatePickerBinding
import com.jenny.deara.diary.DiaryEditActivity

class BoardPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentBoardPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardPopupBinding.inflate(inflater, container, false)

        binding.editBtn.setOnClickListener {
            val intent = Intent(context, BoardEditActivity::class.java)
            startActivity(intent)
        }
        binding.delBtn.setOnClickListener {
            // 삭제 구현
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("nav_board", "fifth")
            startActivity(intent)
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}