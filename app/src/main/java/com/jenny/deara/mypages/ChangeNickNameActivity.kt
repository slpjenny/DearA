package com.jenny.deara.mypages

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

        // firebase 에서 닉네임 불러오기
        if(user!=null) {
            database.child("users").child(user.uid).get().addOnSuccessListener {
                binding.myNick.setText(it.value.toString())

                // 닉네임 처음에 -> pbl 화이팅 하얗게 해서 안보이게했다가
                // 내 닉네임 불러와지면 text 색상도 바뀌게 하기
                binding.myNick.setTextColor(Color.BLACK)

            }.addOnFailureListener {
                binding.myNick.setText("닉네임 정보 없음")
           }
        }


        // [중복 확인] 버튼-> 닉네임 중복 확인
        binding.nickCheckBtn.setOnClickListener {
            var nick = binding.editNickTxt.text.toString()

            // 닉네임 입력창 null 체크
            if(nick.isNullOrEmpty()){
                Toast.makeText(baseContext,"닉네임을 먼저 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{

                // 닉네임이 작성되어야 중복 확인 기능 작동
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        // child 내에 있는 데이터만큼 반복합니다.
                        // 회원가입에서는 제대로 중복확인 기능 돌아가는데 왜 여기서 똑같은 코드인데 안돌아가는거냐????
                        // 심지어 닉네임 똑같은걸로 저장도 되어버림..
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
            if(nick.isNullOrEmpty()){
                Toast.makeText(baseContext,"닉네임을 먼저 입력해주세요",Toast.LENGTH_SHORT).show()
            }else{
                // 닉네임 중복 확인 여부 체크하기
                if(checkNickBtn == 0){
                    Toast.makeText(baseContext,"닉네임 중복 확인을 해주세요",Toast.LENGTH_SHORT).show()
                }else{

                    // 사용자의 uid 에 따라 닉네임을 저장
                    if (user != null) {
                        database.child("users").child(user.uid).setValue(nick)

                        Toast.makeText(baseContext,"닉네임 변경이 완료되었습니다",Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(baseContext,"닉네임 정보 불러오기 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}