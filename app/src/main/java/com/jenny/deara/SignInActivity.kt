package com.jenny.deara

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.diary_list_item.*

class SignInActivity : AppCompatActivity() {

    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //    val myRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {

//        FirebaseApp.initializeApp(baseContext)

        // Initialize Firebase Auth
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 자가진단 페이지로 이동
        binding.gotoSelfDiagnosis.setOnClickListener {
            val intent = Intent(this,SelfDiagnosisActivity::class.java)
            startActivity(intent)
        }

        binding.back1.setOnClickListener {
            onBackPressed()
        }

        // '완료' 버튼 누르면 신규 사용자 이메일,비밀번호로 가입
        binding.saveBtn.setOnClickListener {

            // 회원가입 editText 에 들어가는 내용 string으로 변환
            var email = binding.writeEmailEtxt.text.toString()
            var pwd = binding.writePwdEtxt.text.toString()
            var repwd = binding.rewritePwdEtxt.text.toString()
            var nick = binding.writeNickEtxt.text.toString()

            database = Firebase.database.reference

            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Log.d(TAG, "info :" + user.toString())

                        // 회원가입하고 있는 사용자의 UID 값을 먼저 users 그룹 안에 저장
                        if (user != null) {
                            database.child("users").setValue(user.uid)
                        }

                        // 사용자의 uid 에 따라 닉네임을 저장
                        if (user != null) {
                            database.child("users").child(user.uid).setValue(nick)
                        }


//                    updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                    }
                }
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}