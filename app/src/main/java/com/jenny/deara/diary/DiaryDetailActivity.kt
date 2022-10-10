package com.jenny.deara.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryDetailBinding
import com.jenny.deara.utils.FBRef

class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_detail)


        val key = intent.getStringExtra("key")
        if (key != null) {
            getDiaryData(key)
        }

        //뒤로가기 버튼
        binding.closeBtn.setOnClickListener {
            finish()
        }

        //수정
        binding.modifyBtn.setOnClickListener {
            val intent = Intent(this, DiaryEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        //삭제
        binding.trashBtn.setOnClickListener {
            if (key != null) {
                PopupFragment(key).show(supportFragmentManager, "SampleDialog")
            }
        }
    }

    // firebase realtimedatabase에서 데이터 받아오기
    private fun getDiaryData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(DiaryData::class.java)

                if (dataModel != null) {
                    if (dataModel.sort == "ver1"){
                        binding.title1.text = "좋았던 일"
                        binding.title2.text = "안 좋았던 일"
                        binding.title3.text = "나의 다짐"
                    }else{
                        binding.title1.text = "칭찬할 점"
                        binding.title2.text = "반성할 점"
                        binding.title3.text = "느낀점"
                    }
                }
                if (dataModel != null){
                    binding.contents1.text = dataModel.contents1
                    binding.contents2.text = dataModel.contents2
                    binding.contents3.text = dataModel.contents3
                    binding.randomQ.text = dataModel.r_question
                    binding.randomA.text = dataModel.r_contents
                } else{
                    val intent = Intent(this@DiaryDetailActivity, MainActivity::class.java)
                    intent.putExtra("nav_diary", "fourth")
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("DiaryTEst", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.diaryRef.child(key).addValueEventListener(postListener)
    }
}