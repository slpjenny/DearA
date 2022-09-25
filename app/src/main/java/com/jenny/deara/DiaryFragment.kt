package com.jenny.deara

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jenny.deara.databinding.FragmentDiaryBinding
import com.jenny.deara.diary.DiaryEditActivity

class DiaryFragment : Fragment() {

    private lateinit var binding: FragmentDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)

        binding.diaryplusBtn.setOnClickListener{
            val intent = Intent(context, DiaryEditActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}