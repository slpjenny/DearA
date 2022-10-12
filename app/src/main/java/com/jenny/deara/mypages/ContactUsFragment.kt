package com.jenny.deara.mypages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentContactUsBinding
import com.jenny.deara.databinding.FragmentDatePickerBinding
import com.jenny.deara.databinding.FragmentHomeBinding

class ContactUsFragment : Fragment() {

    private lateinit var binding : FragmentContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding 초기화
        binding = FragmentContactUsBinding.inflate(inflater,container,false)

        // [문의 메일 보내기] 버튼 -> 메일 앱과 연동해서 메일 전송시키기
        binding.sendMailBtn.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        return binding.root
    }


}