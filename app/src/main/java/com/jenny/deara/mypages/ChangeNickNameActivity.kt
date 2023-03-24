package com.jenny.deara.mypages

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.LoginActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityChangeNickNameBinding
import com.jenny.deara.databinding.ActivityMyPageBinding
import kotlin.concurrent.thread

class ChangeNickNameActivity : AppCompatActivity() {

    val binding by lazy { ActivityChangeNickNameBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        // nickFor : 닉네임 중복확인 시, 중복된 닉네임이 있는지 확인
        var nickFor = 0

        // checkNickBtn : 닉네임 중복 확인 버튼 클릭 체크 여부
        var checkNickBtn = 0

        // Auth 초기화
        auth = Firebase.auth

        // 실시간 database reference 불러오기
        database = Firebase.database.reference

        // 현재 로그인 되어있는 정보 불러오기
        val user = auth.currentUser

        thread(start = true){

            if (user != null) {
                database.child("users").child(user.uid).get().addOnSuccessListener {

                    runOnUiThread {
                        binding.myNick.setText(it.value.toString())
                    }

                }.addOnFailureListener {

                    runOnUiThread {
                        binding.myNick.setText("닉네임 정보 없음")

                    }
                }
            }
        }


        // firebase 에서 닉네임 불러오기
//        if (user != null) {
//            database.child("users").child(user.uid).get().addOnSuccessListener {
//                binding.myNick.setText(it.value.toString())
//
//                // 동기 방식으로 흐름 바꾸기!
//
//                // 닉네임 처음에 -> pbl 화이팅 하얗게 해서 안보이게했다가
//                // 내 닉네임 불러와지면 text 색상도 바뀌게 하기
//                binding.myNick.setTextColor(Color.BLACK)
//
//            }.addOnFailureListener {
//                binding.myNick.setText("닉네임 정보 없음")
//            }
//        }


        // [중복 확인] 버튼-> 닉네임 중복 확인
        binding.nickCheckBtn.setOnClickListener {
            var nick = binding.editNickTxt.text.toString()

            // 닉네임 입력창 null 체크
            if (nick.isNullOrEmpty()) {
                Toast.makeText(baseContext, "닉네임을 먼저 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {

                // 닉네임이 작성되어야 중복 확인 기능 작동
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var data = mapOf<String,String>()

                        // 이렇게 하면 users 밑의 uid와 닉네임들만 출력은 가능하다.
//                        Log.d("nicklog",dataSnapshot.child("users").value.toString())

                        data  = dataSnapshot.child("users").value as Map<String, String>

                        // 와 이제 이렇게 하면 닉네임들만 뜬다.
                        Log.d("nicklog", data.values.toString())

                        if (nick in data.values){
                            Toast.makeText(baseContext, "다른 닉네임을 사용해주세요.", Toast.LENGTH_SHORT).show()

                        } else{
                            Toast.makeText(baseContext, "사용하실 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show()

                            // 닉네임 중복 확인 통과 체크
                            checkNickBtn = 1
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        }


        // [저장] 버튼 -> 닉네임 변경되어 저장
        binding.saveBtn.setOnClickListener {
            var nick = binding.editNickTxt.text.toString()


            // 닉네임 입력창 null 체크
            if (nick.isNullOrEmpty()) {
                Toast.makeText(baseContext, "닉네임을 먼저 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 닉네임 중복 확인 여부 체크하기
                if (checkNickBtn == 0) {
                    Toast.makeText(baseContext, "닉네임 중복 확인을 해주세요", Toast.LENGTH_SHORT).show()
                } else {

                    // 사용자의 uid 에 따라 닉네임을 저장
                    if (user != null) {
                        database.child("users").child(user.uid).setValue(nick)

                        Toast.makeText(baseContext, "닉네임 변경이 완료되었습니다", Toast.LENGTH_SHORT).show()

                        // 다시 중복체크 변수 초기화
                        checkNickBtn = 0

                        // Activity 새로고침
                        finish()
                        startActivity(getIntent())

                    } else {
                        Toast.makeText(baseContext, "닉네임 정보 불러오기 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        }


    override fun onRestart() {
        super.onRestart()
    }

    private fun hideKeyboard() {
        if(this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    }
