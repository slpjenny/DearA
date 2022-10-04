package com.jenny.deara.mypages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.jenny.deara.PatternLock.PatternSecureModeActivity
import com.jenny.deara.PatternLock.SettingPatternActivity
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_select_lock.*
import kotlinx.android.synthetic.main.activity_select_lock.constraintLayout3
import kotlinx.android.synthetic.main.activity_setting_pattern.*

class SelectLockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_lock)

        lock_select_switch.setOnClickListener {
            if (lock_select_switch.isChecked) {
                //체크된 상태로 만들 시 코드
                constraintLayout3.isVisible=true
                constraintLayout4.isVisible=true
                constraintLayout5.isVisible=true

//                val dialog = CustomDialog()
//                dialog.show(supportFragmentManager, "CustomDialog")
            } else {
                //체크된 상태 취소 시 코드
                constraintLayout3.isVisible=false
                constraintLayout4.isVisible=false
                constraintLayout5.isVisible=false
            }
        }

        pattern_arrow.setOnClickListener {
            val intent  = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent)
        }
    }
}