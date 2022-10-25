package com.jenny.deara.mypages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.activity_select_lock.*


class SelectLockActivity : AppCompatActivity() {


    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_lock)

        textView.text = if (switchButton.isChecked )
            "암호/지문 잠금" else "암호/지문 잠금"


        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textView.text = "암호/지문 잠금"
                switchButton?.isSelected = switchButton?.isSelected != true
            } else {
                textView.text = "암호/지문 잠금"
                switchButton?.isSelected = switchButton?.isSelected != false
            }
        }

        //switchButton이 isChecked 된 경우 암호 설정+지문인식On
        //btnChangePwd는 switchButton이 isChecked된 상태에만 활성화
        //btnChangePwd 클릭 시 지문인식 또는 이전 암호 확인 후 변경 가능


        back.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}
