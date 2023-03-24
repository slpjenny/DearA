package com.jenny.deara

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jenny.deara.board.BoardMainFragment
import com.jenny.deara.diary.DiaryFragment
import com.jenny.deara.home.HomeFragment
import com.jenny.deara.record.RecordFragment
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

        // 데이터 넘기기
        val diaryFragment = DiaryFragment()
        var bundle = Bundle()
        bundle.putInt("iMonth", getMonth)
        bundle.putInt("iYear", getYear)
        diaryFragment.arguments = bundle

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
                        diaryFragment
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
            Handler().postDelayed({bnv_main.selectedItemId = R.id.fourth
                changeFragment(diaryFragment)},1000L)
        }

        if (nav == "fifth"){
            Handler().postDelayed({bnv_main.selectedItemId = R.id.fifth
            changeFragment(BoardMainFragment())}, 1000L)
        }

    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }
}

