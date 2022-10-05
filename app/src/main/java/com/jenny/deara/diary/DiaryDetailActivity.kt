package com.jenny.deara.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jenny.deara.DiaryFragment
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryDetailBinding

class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_detail)


        //뒤로가기 버튼
        binding.closeBtn.setOnClickListener {
            finish()
        }

        //수정
        binding.modifyBtn.setOnClickListener {
            val intent = Intent(this, DiaryEditActivity::class.java)
            startActivity(intent)
        }

        //삭제
        binding.trashBtn.setOnClickListener {
            PopupFragment().show(supportFragmentManager, "SampleDialog")
        }
    }
}