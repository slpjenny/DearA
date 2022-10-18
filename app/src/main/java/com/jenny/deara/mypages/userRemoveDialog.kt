package com.jenny.deara.mypages

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.jenny.deara.R
import kotlin.coroutines.coroutineContext

class userRemoveDialog(context: Context) {

    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference

    private val dialog = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.userremove_dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val yesBtn = dialog.findViewById<Button>(R.id.yesBtn)
        val noBtn = dialog.findViewById<Button>(R.id.noBtn)

        // 회원가입 탈퇴 ->[예]
        yesBtn.setOnClickListener {
//            onClickListener.onClicked(editText.text.toString())

            // Auth 초기화
            auth = Firebase.auth

            val user = Firebase.auth.currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "사용자 계정 삭제 완료")
                        Toast.makeText(dialog.context,"탈퇴 처리 되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // 회원가입 탈퇴 -> [아니오]
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener{
        fun onClicked(text: String)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickListener(listener: ButtonClickListener){
        onClickListener = listener
    }

}