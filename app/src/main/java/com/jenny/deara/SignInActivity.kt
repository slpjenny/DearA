package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 자가진단 페이지로 이동
        binding.gotoSelfDiagnosis.setOnClickListener {
            val intent = Intent(this,SelfDiagnosisActivity::class.java)
            startActivity(intent)
        }

        binding.back1.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}