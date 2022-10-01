package com.jenny.deara.PatternLock

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jenny.deara.R
import com.jenny.deara.mypages.SelectLockActivity
import kotlinx.android.synthetic.main.activity_setting_pattern.*


class SettingPatternActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_pattern)

        pattern_setting.setOnClickListener {
            val intent  = Intent(this, PatternActivity::class.java)
            startActivity(intent)
        }

        imageView5.setOnClickListener {
            val intent2 = Intent(this, SelectLockActivity::class.java)
            startActivity(intent2)
        }

        pattern_setting_switch.setOnClickListener {
            if (pattern_setting_switch.isChecked) {
                //체크된 상태로 만들 시 코드
                val intent3 = Intent(this, PatternSecureModeActivity::class.java)
                startActivity(intent3)
            } else {
                //체크된 상태 취소 시 코드
                Toast.makeText(
                    this@SettingPatternActivity,
                    "선 숨기기 기능이 해제되었습니다.",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
}
                    /*var type = intent.getIntExtra(PatternActivity.KEY_PATTERN_TYPE, PatternActivity.TYPE_DEFAULT)
        when(type) {
            PatternActivity.TYPE_DEFAULT -> {
                setContentView(R.layout.activity_pattern_default)
                defaultPatternLockView.setOnPatternListener(listener)
            }
            *//*PatternActivity.TYPE_JD_STYLE -> {
                setContentView(R.layout.activity_pattern_jd)
                jdPatternLockView.setOnPatternListener(listener)
            }
            PatternActivity.TYPE_WITH_INDICATOR -> {
                setContentView(R.layout.activity_pattern_with_indicator)
                indicatorPatternLockView.setOnPatternListener(listener)
            }
            PatternActivity.TYPE_9x9 -> {
                setContentView(R.layout.activity_pattern_9x9)
                ninePatternLockView.setOnPatternListener(listener)
            }
            PatternActivity.TYPE_SECURE_MODE -> {
                setContentView(R.layout.activity_pattern_default)
                defaultPatternLockView.enableSecureMode()
                defaultPatternLockView.setOnPatternListener(listener)
            }*//*
        }

    }

    private var listener  = object : PatternLockView.OnPatternListener {

        override fun onStarted() {
            super.onStarted()
        }

        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }

        override fun onComplete(ids: ArrayList<Int>): Boolean {
            var isCorrect = TextUtils.equals("012", getPatternString(ids))
            var tip: String
            if (isCorrect) {
                tip = "correct:" + getPatternString(ids)
            } else {
                tip = "error:" + getPatternString(ids)
            }
            Toast.makeText(this@SettingPatternActivity, tip, Toast.LENGTH_SHORT).show()
            return isCorrect
        }
    }

    private fun getPatternString(ids: ArrayList<Int>) : String {
        var result = ""
        for (id in ids) {
            result += id.toString()
        }
        return result
    }*/