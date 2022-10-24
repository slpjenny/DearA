package com.jenny.deara.mypages

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        // Auth 초기화
        auth = Firebase.auth

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


        binding.saveBtn.setOnClickListener {
            // 변경할 비밀번호 입력 체크
            val pwd = binding.pwdTxt.text.toString()
            val newPwd = binding.newPwdTxt.text.toString()
            val rePwd = binding.rePwdtxt.text.toString()


            // EditText null 체크
            if(pwd.isNullOrEmpty() or newPwd.isNullOrEmpty() or rePwd.isNullOrEmpty()){

                Toast.makeText(baseContext,"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()

            }else{

                // 현재 비밀번호 일치하는지 먼저 확인
                // firebase 사용자 재인증 기능 (타임아웃, 비밀번호 재설정시 사용함)
                val user = Firebase.auth.currentUser

                // ?? Pwd 가 기존 비밀번호랑 다른데 어떻게 사용자 재인증 성공한거임???
                // 보니까 비밀번호 변경은 되는데 newPwd 로 저장되고 있는디... (아니다 이건 지금 두세번째 같은지 잘 확인되는디..)
                val credential = EmailAuthProvider
                    .getCredential(user?.email.toString(), pwd)

                // 사용자 재인증 성공시,
                if (user != null) {
                    user.reauthenticate(credential)
                        .addOnCompleteListener { Log.d(TAG, "사용자 재인증 성공") }
                    Log.d("credential",credential.toString())

                    // 새로운 비밀번호 입력 일치하면 비밀번호 변경 작업 수행
                    if(newPwd == rePwd){

                        val user = Firebase.auth.currentUser
                        val newPassword = newPwd

                        user!!.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User password updated.")
                                    Toast.makeText(baseContext,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        Toast.makeText(baseContext,"새 비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(baseContext,"사용자 재인증 오류", Toast.LENGTH_SHORT).show()
                }




            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}