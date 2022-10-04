package com.jenny.deara.mypages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityFindPwdBinding
import com.jenny.deara.databinding.ActivityLoginBinding

class FindPwdActivity : AppCompatActivity() {

    val binding by lazy { ActivityFindPwdBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_pwd)

        binding.back5.setOnClickListener {
            onBackPressed()
        }
    }

    // Activity 뒤로 가기 기능
    // 이거 활성화가 왜 안되냐?
    override fun onBackPressed() {
        super.onBackPressed()
    }


}