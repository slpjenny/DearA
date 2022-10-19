package com.jenny.deara.mypages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityContactUsBinding
import com.jenny.deara.databinding.ActivityLoginBinding

class ContactUsActivity : AppCompatActivity() {

    val binding by lazy { ActivityContactUsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // [개발자에게 문의하기] 버튼 -> 깃헙 주소 연동
        binding.sendMailBtn.setOnClickListener {

            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/slpjenny"))
            startActivity(intent)

        }

    }
}