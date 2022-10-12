package com.jenny.deara.utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {
    companion object {

        lateinit var auth: FirebaseAuth

        fun getUid() : String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()

        }

        fun getTime() : String {

            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

            return dateFormat
        }

        //다이어리 날짜 저장 format
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

            return "$dateFormat $weekString"
        }

        // 월 가져오기
        fun getMonth() : Int{
            var month = CalendarUtil.selectedDate.get(Calendar.MONTH) + 1

            return month
        }

        //년 가져오기
        fun getYear() : Int{
            var year = CalendarUtil.selectedDate.get(Calendar.YEAR)

            return year
        }

    }
}