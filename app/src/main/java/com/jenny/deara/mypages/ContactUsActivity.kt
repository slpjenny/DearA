package com.jenny.deara.mypages

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

        // [문의 메일 보내기] 버튼 -> 메일 앱과 연동해서 메일 전송시키기
//        binding.sendMailBtn.setOnClickListener {
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_EMAIL, "hulk0514@naver.com")
//                type = "text/plain"
//            }
//
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            startActivity(shareIntent)
//
//        }


    }
}