package com.jenny.deara.mypages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.PatternLock.SettingPatternActivity
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_select_lock.*

class SelectLockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_lock)

        pattern_arrow.setOnClickListener {
            val intent  = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent)
        }
    }
}