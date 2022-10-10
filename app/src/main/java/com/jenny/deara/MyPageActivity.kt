package com.jenny.deara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//import com.jenny.deara.PatternLock.PatternActivity

import com.jenny.deara.databinding.ActivityMyPageBinding
import com.jenny.deara.mypages.ChangeNickNameActivity
import com.jenny.deara.mypages.ChangePwdActivity
import com.jenny.deara.mypages.ContactUsFragment
import com.jenny.deara.mypages.SelectLockActivity
import com.jenny.deara.utils.FBAuth.Companion.auth
import kotlinx.android.synthetic.main.activity_my_page.*
import java.util.regex.Pattern

class MyPageActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val email:String

        // Auth 초기화
        auth = Firebase.auth

        // 실시간 database reference 불러오기
        database = Firebase.database.reference

        // firebase 에서 이메일 불러오기
        val user = auth.currentUser

        email = auth.currentUser?.email.toString()
        binding.yourEmail.setText(email)


        // firebase 에서 닉네임 불러오기
        if(user!=null) {
            database.child("users").child(user.uid).get().addOnSuccessListener {
                binding.yourNick.setText(it.value.toString())

            }.addOnFailureListener {
                Toast.makeText(baseContext,"해당하는 닉네임이 없습니다",Toast.LENGTH_SHORT).show()
            }

        }

        // 비밀번호 변경 페이지로 이동
        binding.changePwdtxt.setOnClickListener {
            val intent = Intent(this,ChangePwdActivity::class.java)
            startActivity(intent)
        }

        // 닉네임 변경 페이지로 이동
        binding.changeNickTxt.setOnClickListener {
            val intent2 = Intent(this, ChangeNickNameActivity::class.java)
            startActivity(intent2)
        }

        // 문의하기 페이지로 이동 (fragment)
        binding.contactUsTxt.setOnClickListener {
            val intent3  = Intent(this, ContactUsFragment::class.java)
            startActivity(intent3)
        }

        // 뒤로가기
        binding.back6.setOnClickListener {
            onBackPressed()
        }

        //잠금 설정
        binding.textView16.setOnClickListener {
            val intent4  = Intent(this, SelectLockActivity::class.java)
            startActivity(intent4)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}