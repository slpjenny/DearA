package com.jenny.deara.alarm

import androidx.fragment.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jenny.deara.databinding.AlarmDialogBinding


class AlarmDialog(): DialogFragment() {

    private var _binding: AlarmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AlarmDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // binding.timePicker.setOnTimeChangedListener { timePicker, hour, minute -> }

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 버튼
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼
        binding.saveBtn.setOnClickListener {
            dismiss()
        }

        // 삭제 버튼
        binding.rmAlarm.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
