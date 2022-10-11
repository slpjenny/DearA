package com.jenny.deara.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryEditBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_diary_edit.*
import kotlinx.android.synthetic.main.fragment_diary.*

class DiaryEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditBinding
    private lateinit var sort : String
    private var month : Int = 0
    private var year : Int = 2022

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_edit)

        val key = intent.getStringExtra("key").toString()
        getDiaryData(key)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            editDiaryData(key)
        }

        binding.toggleBtn1.setOnClickListener {

            if (binding.toggleBtn1.isChecked) {
                // toggle_on
                binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_off)
                binding.title1.text = "칭찬할 점"
                binding.title2.text = "반성할 점"
                binding.title3.text = "느낀점"
                this.sort = "ver2"
            }else{
                //toggle_off
                binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_on)
                binding.title1.text = "좋았던 일"
                binding.title2.text = "안 좋았던 일"
                binding.title3.text = "나의 다짐"
                this.sort = "ver1"
            }

        }
    }

    // 파이어베이스에 데이터 저장
    private fun editDiaryData(key : String){

        Log.d("editTest1", binding.content1Area.text.toString())
        FBRef.diaryRef
            .child(key)
            .setValue(
                DiaryData(
                    binding.content1Area.text.toString(),
                    binding.content2Area.text.toString(),
                    binding.content3Area.text.toString(),
                    binding.randomQ.text.toString(),
                    binding.randomA.text.toString(),
                    sort,
                    FBAuth.getTimeDiary(),
                    month,
                    year,
                    FBAuth.getUid()
                )
            )

        Log.d("editTestRandomA", binding.randomA.text.toString())

        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()
    }

    // 이전 데이터 띄우기
    private fun getDiaryData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(DiaryData::class.java)

                binding.content1Area.setText(dataModel?.contents1)
                binding.content2Area.setText(dataModel?.contents2)
                binding.content3Area.setText(dataModel?.contents3)
                binding.randomQ.text = dataModel?.r_question
                binding.randomA.setText(dataModel?.r_contents)
                binding.date.text = dataModel?.time
                sort = dataModel?.sort.toString()
                month = dataModel?.month!!
                year = dataModel.year
                if (sort == "ver1"){
                    binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_on)
                    binding.title1.text = "좋았던 일"
                    binding.title2.text = "안 좋았던 일"
                    binding.title3.text = "나의 다짐"
                }else{
                    binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_off)
                    binding.title1.text = "칭찬할 점"
                    binding.title2.text = "반성할 점"
                    binding.title3.text = "느낀점"
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("diaryEdit", "loadPost:onCancelled", databaseError.toException())
            }

        }

        FBRef.diaryRef.child(key).addValueEventListener(postListener)
    }

}