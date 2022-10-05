package com.jenny.deara.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentDiaryWriteVer2Binding

class DiaryWriteVer2Fragment : Fragment() {

    private lateinit var binding: FragmentDiaryWriteVer2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary_write_ver2, container, false)

        binding.toggleBtn2.setOnClickListener {
            it.findNavController().navigate(R.id.action_diaryEditVer2Fragment_to_diaryEditVer1Fragment)
        }
        return binding.root
    }

}