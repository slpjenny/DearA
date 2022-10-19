package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardEditBinding
import com.jenny.deara.databinding.ActivityBoardWriteBinding

class BoardEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, BoardInsideActivity::class.java)
            startActivity(intent)
        }
    }
}