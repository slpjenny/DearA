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

    lateinit var time: String
    lateinit var title: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AlarmDialogBinding.inflate(inflater, container, false)
        val view = binding.root


        dialog?.setCanceledOnTouchOutside(true) // dialog 밖 누르면 창 없어지게
        dialog?.setCancelable(true) //dialog 취소 가능하게 하기 위한

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 타임피커 설정
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            time= hour.toString() +":"+ minute.toString()
            // val time: String = "$hour:$minute"
        }

        // 취소 버튼
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼
        binding.saveBtn.setOnClickListener {
            title = binding.setAlarmName.text.toString()
            onClickedListener.onClicked(time, title)
            dismiss()
        }

        // 삭제 버튼
        binding.rmAlarm.setOnClickListener {
            dismiss()
        }

        return view
    }

    interface ButtonClickListener{
        fun onClicked(time: String, title: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
