package com.jenny.deara.mypages

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityFindPwdBinding
import com.jenny.deara.databinding.ActivityLoginBinding

class FindPwdActivity : AppCompatActivity() {

    val binding by lazy { ActivityFindPwdBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.back5.setOnClickListener {
            onBackPressed()
        }


        // 입력된 이메일로 비밀번호 재설정 메일 전송
        binding.findPwdBtn.setOnClickListener {

            hideKeyboard()
            val emailAddress = binding.writeEmail.text.toString()

            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        Toast.makeText(baseContext,"입력하신 이메일로 전송되었습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }




    // Activity 뒤로 가기 기능
    // 이거 활성화가 왜 안되냐?
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun hideKeyboard() {
        if(this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}