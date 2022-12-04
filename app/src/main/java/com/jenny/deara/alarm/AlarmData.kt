package com.jenny.deara.alarm

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

data class AlarmData(
    val time: String = "",
    val title: String = "",
    val day: String = "",
    val uid: String = "",
    val alarmId: Int = 0,
    val onOff: Boolean = true,
)
