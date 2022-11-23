package com.jenny.deara.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardImageViewBinding

class BoardImageViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardImageViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_image_view)

        val url = intent.getByteExtra()

        binding.x.setOnClickListener {
            finish()
        }

        binding.imageView.setImageURI(url)
    }
}