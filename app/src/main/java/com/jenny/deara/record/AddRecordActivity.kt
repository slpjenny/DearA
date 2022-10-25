package com.jenny.deara.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityAddRecordBinding
import com.jenny.deara.databinding.ActivitySignInBinding

class AddRecordActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddRecordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 뒤로 가기
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }



    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}