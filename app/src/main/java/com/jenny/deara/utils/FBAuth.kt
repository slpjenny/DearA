package com.jenny.deara.utils

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
//import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {
    companion object {

        lateinit var auth: FirebaseAuth
        lateinit var Nick: String

        fun getUid() : String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()

        }

        fun getTime() : String {

            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

            return dateFormat
        }

        fun getTimeBoard(): String {
            val currentDateTime = CalendarUtil.selectedDate.time

            return SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA).format(currentDateTime)
        }

        //현재 시간 불러오기
        fun getTimeDiary() : String{
            val currentDateTime = CalendarUtil.selectedDate.time
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(currentDateTime)
            val dayOfWeek = CalendarUtil.selectedDate.get(Calendar.DAY_OF_WEEK)

            var weekString = ""

            weekString = when (dayOfWeek) {
                1 -> { "일요일" }
                2 -> { "월요일" }
                3 -> { "화요일" }
                4 -> { "수요일" }
                5 -> { "목요일" }
                6 -> { "금요일" }
                else -> { "토요일" }
            }

            Log.d("currentTime", dateFormat)
            return "$dateFormat $weekString"
        }

        fun setFormat(year: Int, month: Int, day: Int): String{
            val week = getWeek(year, month, day)

            return year.toString() + "년 "+ month.toString() + "월 " + day.toString() + "일 " + week
        }

        //년 가져오기
        fun getYear() : Int{
            var year = CalendarUtil.selectedDate.get(Calendar.YEAR)

            return year
        }

        // 월 가져오기
        fun getMonth() : Int{
            var month = CalendarUtil.selectedDate.get(Calendar.MONTH) + 1

            return month
        }

        // 일 가져오기
        fun getDay() : Int{
            var day = CalendarUtil.selectedDate.get(Calendar.DATE)

            return day
        }

        // 요일 구하기
        fun getWeek(year: Int, month: Int, day: Int): String{
            var date : Calendar = Calendar.getInstance()
            date.set(year, month, day)

            val week = when (date.get(Calendar.DAY_OF_WEEK)) {
                1 -> { "목요일" }
                2 -> { "금요일" }
                3 -> { "토요일" }
                4 -> { "일요일" }
                5 -> { "월요일" }
                6 -> { "화요일" }
                else -> { "수요일" }
            }

            Log.d("currentTime: week ->", week)
            return week
        }

        // 사용자 닉네임 가져오기
        fun getNick(uid: String) : String{
//            return FBRef.userRef.child(uid).toString()
            return "익명"
        }

    }
}