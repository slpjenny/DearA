package com.jenny.deara.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryWriteBinding

class DiaryWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_write)

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