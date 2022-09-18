package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jenny.deara.databinding.ActivityLoginBinding
import com.jenny.deara.mypages.FindPwdActivity

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // '회원가입' textView 클릭시 SignInActivity 페이지로 이동
        binding.signInTxt.setOnClickListener {
            val intentSignIn = Intent(this, SignInActivity::class.java)
            startActivity(intentSignIn)

        }

        // '비밀번호 찾기' textView 클릭시, FindPwdActivity로 이동
        binding.findPwdTxt.setOnClickListener {
            val intentFindPwd = Intent(this, FindPwdActivity::class.java)
            startActivity(intentFindPwd)

        }

    }
}