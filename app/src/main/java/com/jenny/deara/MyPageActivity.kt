package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.PatternLock.PatternActivity
import com.jenny.deara.databinding.ActivityMyPageBinding
import com.jenny.deara.mypages.ChangeNickNameActivity
import com.jenny.deara.mypages.ChangePwdActivity
import com.jenny.deara.mypages.ContactUsFragment
import com.jenny.deara.mypages.SelectLockActivity
import kotlinx.android.synthetic.main.activity_my_page.*
import java.util.regex.Pattern

class MyPageActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 비밀번호 변경 페이지로 이동
        binding.changePwdtxt.setOnClickListener {
            val intent = Intent(this,ChangePwdActivity::class.java)
            startActivity(intent)
        }

        // 닉네임 변경 페이지로 이동
        binding.changeNickTxt.setOnClickListener {
            val intent2 = Intent(this, ChangeNickNameActivity::class.java)
            startActivity(intent2)
        }

        // 문의하기 페이지로 이동 (fragment)
        binding.contactUsTxt.setOnClickListener {
            val intent3  = Intent(this, ContactUsFragment::class.java)
            startActivity(intent3)
        }

        // 뒤로가기
        binding.back6.setOnClickListener {
            onBackPressed()
        }

        //잠금 설정
        binding.textView16.setOnClickListener {
            val intent4  = Intent(this, SelectLockActivity::class.java)
            startActivity(intent4)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}