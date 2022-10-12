package com.jenny.deara.home

import android.widget.CheckBox

data class ToDoData (
    //var todoList : ArrayList<ToDoModel>,
    var todo : String = "",
    var check : Boolean ,
    val time : String = "",
    var uid : String = ""
)