package com.jenny.deara.mypages

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.activity_app_cover.*
import kotlinx.android.synthetic.main.activity_select_lock.*
import kotlinx.android.synthetic.main.activity_select_lock.back

class AppCoverActivity : AppCompatActivity() {

    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_cover)

        back.setOnClickListener {
            onBackPressed()
        }

        button1.setOnClickListener{
            packageManager?.setComponentEnabledSetting(
                ComponentName(applicationContext.packageName, applicationContext.packageName + ".MainActivityAlias1"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )

            packageManager?.setComponentEnabledSetting(
                ComponentName(applicationContext.packageName, applicationContext.packageName + ".MainActivityAlias2"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
            )
            Toast.makeText(this@AppCoverActivity, "선택한 색상으로 변경됨", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener{
            packageManager?.setComponentEnabledSetting(
                ComponentName(applicationContext.packageName, applicationContext.packageName + ".MainActivityAlias1"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
            )

            packageManager?.setComponentEnabledSetting(
                ComponentName(applicationContext.packageName, applicationContext.packageName + ".MainActivityAlias2"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
            Toast.makeText(this@AppCoverActivity, "선택한 색상으로 변경됨", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}