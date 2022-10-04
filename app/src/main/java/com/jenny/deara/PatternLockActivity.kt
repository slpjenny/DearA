package com.jenny.deara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.jenny.deara.PatternLock.PatternActivity
import com.jenny.deara.PatternLock.SettingPatternActivity
import com.jenny.deara.componet.PatternLockView
import kotlinx.android.synthetic.main.activity_pattern.*

class PatternLockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var type = intent.getIntExtra(SettingPatternActivity.KEY_PATTERN_TYPE, SettingPatternActivity.TYPE_DEFAULT)
        when(type) {
            SettingPatternActivity.TYPE_DEFAULT -> {
                setContentView(R.layout.activity_pattern)
                pattern_LockView.setOnPatternListener(listener)
            }

            SettingPatternActivity.TYPE_SECURE_MODE -> {
                setContentView(R.layout.activity_pattern)
                pattern_LockView.enableSecureMode()
                pattern_LockView.setOnPatternListener(listener)
            }
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
            Toast.makeText(this@PatternLockActivity, tip, Toast.LENGTH_SHORT).show()
            return isCorrect
        }
    }

    private fun getPatternString(ids: ArrayList<Int>) : String {
        var result = ""
        for (id in ids) {
            result += id.toString()
        }
        return result
    }
}