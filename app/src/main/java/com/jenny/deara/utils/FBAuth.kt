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
            val currentDate = Calendar.getInstance()
            val currentDateTime = currentDate.time
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).format(currentDateTime)
            val dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK)

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

        fun getMonth() : Int{
            var dateCalendar = Calendar.getInstance()
            var month = dateCalendar.get(Calendar.MONTH) + 1

            return month
        }

        fun getYear() : Int{
            var dateCalendar = Calendar.getInstance()
            var year = dateCalendar.get(Calendar.YEAR)

            return year
        }

    }
}