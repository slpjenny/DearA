package com.jenny.deara

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jenny.deara.databinding.ActivityLoginBinding
import com.jenny.deara.mypages.FindPwdActivity

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Auth 초기화
        auth = Firebase.auth

        // '회원가입' textView 클릭시 SignInActivity 페이지로 이동
        binding.signInTxt.setOnClickListener {
            val intentSignIn = Intent(this, SignInActivity::class.java)
            startActivity(intentSignIn)

        }

        // '비밀번호 찾기' textView 클릭시, FindPwdActivity로 이동
        binding.findPwdTxt.setOnClickListener {
            val intentFindPwd = Intent(this, FindPwdActivity::class.java)
            startActivity(intentFindPwd)
                }

        // ' 로그인' 버튼 누르면 메인으로 이동
        binding.loginBtn.setOnClickListener {

            var email = binding.writeEmail.text.toString()
            var password = binding.writePwd.text.toString()


            // 로그인 정보 미입력시 예외처리
            if (email.isNullOrEmpty()){
                Toast.makeText(baseContext,"이메일을 입력해주세요",Toast.LENGTH_LONG).show()
            } else if(password.isNullOrEmpty()){
                Toast.makeText(baseContext,"비밀번호를 입력해주세요",Toast.LENGTH_LONG).show()
            } else{

                // 항목 모두 작성 시 로그인 진행

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("로그인", "로그인 성공")
                            Toast.makeText(baseContext, "로그인 인증 성공.",Toast.LENGTH_SHORT).show()

                            // user: 로그인 된 현재 사용자
                            val user = auth.currentUser

                            // 로그인 성공 시 메인화면으로 이동
                            val intentMain = Intent(this, MainActivity::class.java)
                            startActivity(intentMain)


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("로그인", "로그인 실패", task.exception)
                            Toast.makeText(baseContext, "로그인 인증 실패.",
                                Toast.LENGTH_SHORT).show()
                            //
                        }
                    }

            }

        }
    }
}