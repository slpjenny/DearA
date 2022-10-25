package com.jenny.deara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jenny.deara.board.BoardMainFragment
import com.jenny.deara.record.RecordFragment
import com.jenny.deara.home.HomeFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        var nav = intent.getStringExtra("nav")
        var dateCalendar = Calendar.getInstance()
        var defaultMonth = dateCalendar.get(Calendar.MONTH) + 1
        var defaultYear = dateCalendar.get(Calendar.YEAR)
        var getMonth = intent.getIntExtra("iMonth", defaultMonth)
        var getYear = intent.getIntExtra("iYear", defaultYear)

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
                        DiaryFragment(defaultMonth, defaultYear)
                    }
                    else -> {
                        BoardMainFragment()
                    }
                }
            )
            true
        }
        bnv_main.selectedItemId = R.id.third

        if (nav == "fourth"){
            bnv_main.selectedItemId = R.id.fourth
            changeFragment(DiaryFragment(getMonth, getYear))
        }
        if (nav == "fifth"){
            bnv_main.selectedItemId = R.id.fifth
            changeFragment(BoardMainFragment())
        }


    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }
}

