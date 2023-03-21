package com.jenny.deara

import android.app.Activity
import android.content.ContentProvider
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.databinding.ActivityMyPageBinding
import com.jenny.deara.home.TodoDialog
import com.jenny.deara.mypages.*
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

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
            val intent3  = Intent(this, ContactUsActivity::class.java)
            startActivity(intent3)
        }

//        // 개인정보처리방침 페이지로 이동
//
//        binding.textView18.setOnClickListener {
//            val intent8  = Intent(this, PrivacyWebViewActivity::class.java)
//            startActivity(intent8)
//        }

        // 개인정보처리방침 페이지로 이동
        binding.textView18.setOnClickListener {
            var intent8 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/54e15dcbe37c4f09a5e9392228a56f7d")
            )
            startActivity(intent8)
        }
        // 이용약관 페이지로 이동
        binding.textView19.setOnClickListener {
            var intent9 = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/b07d03ad439443a488375d49ed2acdf7")
            )
            startActivity(intent9)
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

        // 로그아웃
        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()

            val intent5 = Intent(this, LoginActivity::class.java)

            // 기존 스택 모두 지우기
            // 뒤로가기 버튼 누를시, 기존 정보로 로그인 되는 경우 방지
            finishAffinity()
            startActivity(intent5)

        }


        // 회원탈퇴
        binding.userRemoveBtn.setOnClickListener {

            // 탈퇴여부 묻는 custom dialog 띄우기
            val dialog = userRemoveDialog(this)
            dialog.showDialog()

        }

        // 커뮤니티 이용규칙
        binding.communityRule.setOnClickListener {

            val intent7  = Intent(this, CommunityRuleActivity::class.java)
            startActivity(intent7)
        }

        // 앱 커버 변경
        binding.textView15.setOnClickListener {

            val intent6  = Intent(this, AppCoverActivity::class.java)
            startActivity(intent6)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    // 새로고침
    override fun onResume() {
        super.onResume()

        // firebase 에서 이메일 불러오기
        val user = auth.currentUser


        // firebase 에서 닉네임 불러오기
        if(user!=null) {
            database.child("users").child(user.uid).get().addOnSuccessListener {
                binding.yourNick.setText(it.value.toString() + " 님")
                binding.yourNick.setTextColor(Color.BLACK)

            }.addOnFailureListener {
                binding.yourNick.setText("닉네임 정보 없음")
                binding.yourNick.setTextColor(Color.BLACK)
            }
        }

    }


}