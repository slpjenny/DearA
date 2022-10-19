package com.jenny.deara.home

import android.widget.CheckBox
import com.google.firebase.database.Exclude

data class ToDoData (
    var todo : String = "",
    var check : Boolean = false ,
    var time : String = "",
    var month : Int = 0,
    var date : Int = 0,
    var uid : String = "",
)
//{
//    @Exclude
//    // 필드명 : String, 타입 :Any(필드는 다양한 타입이 있음)
//    fun toMap() : HashMap<String, Any>{
//
//        // 해시 맵 만들기
//        val result : HashMap<String, Any> = HashMap()
//
//        result["todo"] = todo
//        result["check"] = check
//        result["time"] = time
//        result["uid"] = uid
//
//        return result
//    }
//}