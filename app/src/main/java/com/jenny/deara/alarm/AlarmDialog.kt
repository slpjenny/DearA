package com.jenny.deara.alarm

import android.content.Context
import androidx.fragment.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import com.jenny.deara.R
import com.jenny.deara.databinding.AlarmDialogBinding
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.alarm_dialog.*

class AlarmDialog() : DialogFragment() {

    private var _binding: AlarmDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var hour: String
    lateinit var minute: String
    lateinit var title: String
    lateinit var day: String
    lateinit var week: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AlarmDialogBinding.inflate(inflater, container, false)
        // val view = binding.root

        binding.activityTimePicker.setOnClickListener {
            hideKeyboard()
        }

        binding.layout.setOnClickListener {
            hideKeyboard()
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        dialog?.setCanceledOnTouchOutside(true) // dialog 밖 누르면 창 없어지게
        dialog?.setCancelable(true) //dialog 취소 가능하게 하기 위한

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 요일 설정
        week = Array(7) { item -> "" }

        binding.monCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[0] = "월 "
            else week[0] = ""
        }

        binding.tueCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[1] = "화 "
            else week[1] = ""
        }

        binding.wedCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[2] = "수 "
            else week[2] = ""
        }

        binding.thuCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[3] = "목 "
            else week[3] = ""
        }

        binding.friCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[4] = "금 "
            else week[4] = ""
        }

        binding.satCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[5] = "토 "
            else week[5] = ""
        }

        binding.sunCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) week[6] = "일"
            else week[6] = ""
        }


        // 타임피커 설정
        binding.timePicker.setIs24HourView(true)
        hour = binding.timePicker.hour.toString()
        minute = binding.timePicker.minute.toString()

        binding.timePicker.setOnTimeChangedListener { timePicker, h, m ->
            hour = h.toString()
            minute = m.toString()
        }

        // 취소 버튼
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼
        binding.saveBtn.setOnClickListener {
            day = week.joinToString("")
            title = binding.setAlarmName.text.toString()
            if (title == "" || day == "") {
                Toast.makeText(requireActivity(), "빈 칸을 채워주세요", Toast.LENGTH_LONG).show()
            } else {
                onClickedListener.onClicked(hour, minute, title, day)
                dismiss()
            }
        }

        return binding.root

    }

    interface ButtonClickListener{
        fun onClicked(hour: String, minute: String,  title: String, day: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard() {
        if(activity != null && requireActivity().currentFocus != null) {
            val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}






