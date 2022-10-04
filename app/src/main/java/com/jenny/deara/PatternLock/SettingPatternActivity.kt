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

    companion object {
        const val KEY_PATTERN_TYPE = "type"

        const val TYPE_DEFAULT = 0
        const val TYPE_SECURE_MODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_pattern)

        pattern_setting.setOnClickListener { _-> startPatternActivity(TYPE_DEFAULT) }


        //뒤로 가기 버튼 누르면
        imageView5.setOnClickListener {
            val intent = Intent(this, SelectLockActivity::class.java)
            startActivity(intent)
        }

        pattern_setting_switch.setOnClickListener {

            if (pattern_setting_switch.isChecked) {
                //체크된 상태로 만들 시 코드
                val intent3 = Intent(this, PatternSecureModeActivity::class.java)
                startActivity(intent3)

//                val dialog = CustomDialog()
//                dialog.show(supportFragmentManager, "CustomDialog")
            } else {
                //체크된 상태 취소 시 코드
                Toast.makeText(
                    this@SettingPatternActivity,
                    "선 숨기기 기능이 해제되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }
    private fun startPatternActivity(type: Int) {
        val intent = Intent(this, PatternActivity::class.java)
        intent.putExtra(KEY_PATTERN_TYPE, type)
        startActivity(intent)
    }
}