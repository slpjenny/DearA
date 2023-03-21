package com.jenny.deara

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jenny.deara.databinding.ActivitySignInBinding
import kotlinx.android.synthetic.main.diary_list_item.*


class SignInActivity : AppCompatActivity() {

    val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {

        // checkNickBtn : 닉네임 중복 확인 버튼 클릭 체크 여부
        var checkNickBtn = 0

        // nickFor : 닉네임 중복확인 시, 중복된 닉네임이 있는지 확인
        var nickFor = 0


        // Auth 초기화
        auth = Firebase.auth

        // 실시간 database reference 불러오기
        database = Firebase.database.reference.child("users")

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        // 자가진단 페이지로 이동
        binding.gotoSelfDiagnosis.setOnClickListener {
            val intent = Intent(this,SelfDiagnosisActivity::class.java)
            startActivity(intent)
        }

        binding.back1.setOnClickListener {
            onBackPressed()
        }

        // 이메일 유효성 체크
        binding.emailCheck.setOnClickListener {

            hideKeyboard()
//            //  ActionCodeSettings 객체를 구성
//            val actionCodeSettings = actionCodeSettings {
//                // URL you want to redirect back to. The domain (www.example.com) for this
//                // URL must be whitelisted in the Firebase Console.
//                url = "https://deara.page.link"
//                // This must be true
//                handleCodeInApp = true
//                setIOSBundleId("com.jenny.ios")
//                setAndroidPackageName(
//                    "com.jenny.deara",
//                    true, /* installIfNotAvailable */
//                    "12" /* minimumVersion */)
//            }
//
//            val email = auth.currentUser?.email.toString()
//
//            // 사용자의 이메일로 인증 링크를 전송하고, 사용자가 동일한 기기에서 이메일 로그인을 완료할 경우를 대비하여 사용자의 이메일을 저장합니다.
//            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d("email", "Email sent.")
//                    }else{
//                        Log.d("email","이메일 안됨")
//                    }
//                }

        }


        // 닉네임 중복 확인
        binding.checkNick.setOnClickListener {
            hideKeyboard()
            var nick = binding.writeNickEtxt.text.toString()

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    // child 내에 있는 데이터만큼 반복합니다.
                    for (data in dataSnapshot.children) {
                        if (nick == data.value.toString()){
                            nickFor = 1
                            Toast.makeText(baseContext,"다른 닉네임을 사용해주세요",Toast.LENGTH_SHORT).show()
                            break
                        }
                    }

                    // 닉네임 중복 안되었을 때
                    if (nickFor == 0){
                        Toast.makeText(baseContext,"사용하실 수 있는 닉네임입니다.",Toast.LENGTH_SHORT).show()

                        // 닉네임 중복 확인 통과
                        checkNickBtn = 1

                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
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

                // 닉네임 중복 확인했는지 확인
                if(checkNickBtn == 0){
                    Toast.makeText(baseContext,"닉네임 중복 확인을 해주세요",Toast.LENGTH_SHORT).show()
                }else{

                    // 비밀번호 재입력 확인
                    if (pwd == repwd){
                        // 이상 없으면 회원가입
                        auth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(this) { task ->

                                if (task.isSuccessful) {

                                    // user : 지금 가입한 회원
                                    var user = auth.currentUser

                                    Toast.makeText(baseContext,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()

                                    // 사용자의 uid 에 따라 닉네임을 저장
                                    if (user != null) {
                                        database.child("users").child(user.uid).setValue(nick)
                                    }

                                    Toast.makeText(baseContext,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()

                                    // 회원가입 완료시 [로그인] 페이지로 이동하기
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)

                                    // 회원가입 과정에서 오류나면 다음 메세지 생성
                                } else  {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "회원가입 오류", task.exception)
                                    Toast.makeText(baseContext, "회원가입 실패.(해당 이메일로 이미 가입하셨습니다)",
                                        Toast.LENGTH_SHORT).show()
                                }

                            }

                    }else {
                        Toast.makeText(baseContext,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
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

}

