package com.jenny.deara.mypages

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityChangePwdBinding
import com.jenny.deara.databinding.ActivityMyPageBinding


class ChangePwdActivity : AppCompatActivity() {

    val binding by lazy { ActivityChangePwdBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        // Auth 초기화
        auth = Firebase.auth

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.saveBtn.setOnClickListener {

            // 비밀번호 재설정하는 메일 보내기
            val user = Firebase.auth.currentUser

            if (user != null) {
                user?.let {
                    val email = user.email

                    if (email != null) {
                        Firebase.auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(baseContext,"비밀번호 재설정 이메일을 보냈습니다.",Toast.LENGTH_SHORT)
                                }else{
                                    Toast.makeText(baseContext,"메일 전송을 실패했습니다.",Toast.LENGTH_SHORT)
                                }
                            }
                }
            }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun hideKeyboard() {
        if(this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    // 사용자 이메일 가져오기
//    private fun getEmail() : String? {
//
//    }

}

