package com.jenny.deara.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object{
        private val database = Firebase.database

        val reportRef = database.getReference("report")
        val recordRef = database.getReference("record")
        val pillRef = database.getReference("pill")
        val diaryRef = database.getReference("diary")
        val randomQuestionRef = database.getReference("randomQuestion")
        val alarmRef = database.getReference("alarm")
        val todoRef = database.getReference("todo")
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
        val commentReplyRef = database.getReference("commentReply")

        val userRef = database.reference.child("users")
    }
}