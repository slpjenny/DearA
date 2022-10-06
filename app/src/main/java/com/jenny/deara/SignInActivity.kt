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

        // Auth 초기화
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


        // 이메일 중복 확인
        binding.checkEmail.setOnClickListener {
            var email = binding.writeEmailEtxt.text.toString()


        }


        // '완료' 버튼 누르면 신규 사용자 이메일,비밀번호로 가입
        binding.saveBtn.setOnClickListener {

            // 회원가입 editText 에 들어가는 내용 string으로 변환
            var email = binding.writeEmailEtxt.text.toString()
            var pwd = binding.writePwdEtxt.text.toString()
            var repwd = binding.rewritePwdEtxt.text.toString()
            var nick = binding.writeNickEtxt.text.toString()

            database = Firebase.database.reference

            if (email.isNullOrEmpty()){
                Toast.makeText(baseContext,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else if(pwd.isNullOrEmpty()){
                Toast.makeText(baseContext,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            }else if(repwd.isNullOrEmpty()){
                Toast.makeText(baseContext,"비밀번호 재입력 칸을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else if(nick.isNullOrEmpty()){
                Toast.makeText(baseContext,"사용하실 닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{ // 모든 칸에 값이 제대로 입력되었다면,

                // 비밀번호 재입력 확인
                if (pwd == repwd){
                    // 이상 없으면 회원가입
                    auth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")

                                // user : 지금 가입한 회원
                                var user = auth.currentUser


                                // 사용자의 uid 에 따라 닉네임을 저장
                                if (user != null) {
                                    database.child("users").child(user.uid).setValue(nick)
                                }

                                Toast.makeText(baseContext,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()

                                // 회원가입 과정에서 오류나면 다음 메세지 생성
                            } else  {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            }

                        }

                }else {
                    Toast.makeText(baseContext,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                }
            }

        }

        }


    override fun onBackPressed() {
        super.onBackPressed()
    }

}

