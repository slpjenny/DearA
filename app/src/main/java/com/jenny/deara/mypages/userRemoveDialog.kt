package com.jenny.deara.mypages

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.LoginActivity
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import java.security.AccessController.getContext
import kotlin.coroutines.coroutineContext

class userRemoveDialog(context: Context) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val dialog = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.userremove_dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val yesBtn = dialog.findViewById<Button>(R.id.yesBtn)
        val noBtn = dialog.findViewById<Button>(R.id.noBtn)

        // 회원가입 탈퇴 ->[예]
        yesBtn.setOnClickListener {

            // Auth 초기화
            auth = Firebase.auth

            // 실시간 database reference 불러오기
            database = Firebase.database.reference

            val user = Firebase.auth.currentUser!!

            // 회원삭제 코드 (Authentication에 담긴 정보만 삭제)
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Realtime Database의 닉네임도 삭제
                        database.child("users").child(user.uid).removeValue().addOnSuccessListener {
                            Log.d("nickRemove","닉네임 삭제 완료")
                        }.addOnFailureListener {
                            Toast.makeText(dialog.context,"낙네임 삭제 오류",Toast.LENGTH_SHORT).show()
                        }

                        Log.d(ContentValues.TAG, "사용자 계정 삭제 완료")
                        Toast.makeText(dialog.context,"탈퇴 처리  되었습니다", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()

                        // 회원탈퇴하면 로그인 페이지로 이동
                        val intent = Intent(dialog.context, LoginActivity::class.java)
                        dialog.context.startActivity(intent)
                    }
                }
        }

        // 회원가입 탈퇴 -> [아니오]
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface ButtonClickListener{
        fun onClicked(text: String)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickListener(listener: ButtonClickListener){
        onClickListener = listener
    }

}