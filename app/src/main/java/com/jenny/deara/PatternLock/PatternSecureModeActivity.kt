package com.jenny.deara.PatternLock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import com.jenny.deara.mypages.SelectLockActivity
import kotlinx.android.synthetic.main.activity_pattern_secure_mode.*
import kotlinx.android.synthetic.main.activity_setting_pattern.*

class PatternSecureModeActivity : AppCompatActivity() {

    companion object {
        const val KEY_PATTERN_TYPE = "type"
        const val TYPE_SECURE_MODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_secure_mode)

        box2.setOnClickListener { /*_-> startPatternActivity(TYPE_SECURE_MODE)*/
            onBackPressed()
        }

        secure_mode_x.setOnClickListener {
            val intent = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent)
        }

        box1.setOnClickListener {
            val intent2 = Intent(this, SettingPatternActivity::class.java)
            startActivity(intent2)
        }
    }

    private fun startPatternActivity(type: Int) {
        val intent = Intent(this, PatternActivity::class.java)
        intent.putExtra(KEY_PATTERN_TYPE, type)
        startActivity(intent)
    }
}