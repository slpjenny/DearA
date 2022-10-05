package com.jenny.deara.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryEditBinding

class DiaryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_edit)

        // 엑스 버튼
        binding.closeBtn.setOnClickListener {
            finish()
        }

        //저장 버튼
        binding.saveBtn.setOnClickListener {
            finish()
        }
    }
}