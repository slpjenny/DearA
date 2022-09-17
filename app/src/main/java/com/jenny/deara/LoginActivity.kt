package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jenny.deara.mypages.FindPwdActivity

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // '회원가입' textView 클릭시 SignInActivity 페이지로 이동
        val signInTxt = findViewById<TextView>(R.id.signInTxt)
        signInTxt.setOnClickListener {
            val intentSignIn = Intent(this, SignInActivity::class.java)
            startActivity(intentSignIn)
        }

        // '비밀번호 찾기' textView 클릭시, FindPwdActivity로 이동
        val findPwdTxt = findViewById<TextView>(R.id.findPwdTxt)
        findPwdTxt.setOnClickListener {
            val intentFindPwd = Intent(this, FindPwdActivity::class.java)
            startActivity(intentFindPwd)
        }


    }
}