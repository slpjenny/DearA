package com.jenny.deara.PatternLock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import com.jenny.deara.mypages.SelectLockActivity
import kotlinx.android.synthetic.main.activity_pattern_secure_mode.*
import kotlinx.android.synthetic.main.activity_setting_pattern.*

class PatternSecureModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_secure_mode)

        secure_mode_x.setOnClickListener {
            val intent = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent)
        }

        box1.setOnClickListener {
            val intent2 = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent2)
        }
    }
}