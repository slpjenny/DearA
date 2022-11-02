package com.jenny.deara.home

import android.widget.CheckBox
import com.google.firebase.database.Exclude

data class ToDoData (
    var todo : String = "",
    var check : Boolean = false ,
    var time : String = "",
    var year : String ="",
    var month : String = "",
    var date : String = "",
    var uid : String = "",
    var key : String = ""
)
