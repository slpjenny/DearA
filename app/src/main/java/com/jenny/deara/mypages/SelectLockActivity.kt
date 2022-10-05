package com.jenny.deara.mypages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_select_lock.*
import kotlinx.android.synthetic.main.activity_select_lock.constraintLayout3

class SelectLockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_lock)

        imageView5.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}