package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.diary.PopupFragment

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)


        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myPageBtn.setOnClickListener{
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.popupBtn.setOnClickListener {
            BoardPopupFragment().show(supportFragmentManager, "SampleDialog")
        }

    }
}