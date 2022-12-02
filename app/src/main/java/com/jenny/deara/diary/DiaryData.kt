package com.jenny.deara.diary

data class DiaryData (
        var contents1 : String = "",
        var contents2 : String = "",
        var contents3: String = "",
        var r_question: String = "",
        var r_contents : String = "",
        var sort: String = "",
        val time : String = "",
        val year: Int = 2022,
        val month: Int = 1,
        val day: Int = 1,
        val uid : String = ""
)