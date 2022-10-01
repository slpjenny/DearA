package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.jenny.deara.board.BoardMainFragment
import com.jenny.deara.fragments.RecordFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        var nav_diary = intent.getStringExtra("nav_diary")

        bnv_main.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.first -> {
                        AlarmFragment()
                    }
                    R.id.second -> {
                        RecordFragment()
                    }
                    R.id.third -> {
                        HomeFragment()
                    }
                    R.id.fourth -> {
                        DiaryFragment()
                    }
                    else -> {
                        BoardMainFragment()
                    }
                }
            )
            true
        }
        bnv_main.selectedItemId = R.id.third

        if (nav_diary == "fourth"){
            changeFragment(DiaryFragment())
            bnv_main.selectedItemId = R.id.fourth
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }
}