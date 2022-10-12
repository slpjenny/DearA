package com.jenny.deara.mypages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityChangeNickNameBinding
import com.jenny.deara.databinding.ActivityMyPageBinding

class ChangeNickNameActivity : AppCompatActivity() {

    val binding by lazy { ActivityChangeNickNameBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Auth 초기화
        auth = Firebase.auth

        // 실시간 database reference 불러오기
        database = Firebase.database.reference

        // 현재 로그인 되어있는 정보 불러오기
        val user = auth.currentUser

        // firebase 에서 닉네임 불러오기
        if(user!=null) {
            database.child("users").child(user.uid).get().addOnSuccessListener {
                binding.myNick.setText(it.value.toString())

            }.addOnFailureListener {
                binding.myNick.setText("닉네임 정보 없음")
           }

        }
    }
}