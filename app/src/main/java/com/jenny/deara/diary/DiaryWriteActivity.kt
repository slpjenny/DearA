package com.jenny.deara.diary

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.PointF.length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityDiaryWriteBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import java.nio.file.Files.size
import java.util.*

class DiaryWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryWriteBinding
    private var sort : String = "ver1"

    val randomQList = mutableListOf<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_write)

        // init settings //
        getFBRandomQData()
        Log.d("randomQList", "$randomQList")
        binding.title1.text = "좋았던 일"
        binding.title2.text = "안 좋았던 일"
        binding.title3.text = "나의 다짐"

        // 현재 날짜
        binding.date.text = FBAuth.getTimeDiary()

        // 엑스 버튼
        binding.closeBtn.setOnClickListener {
            finish()
        }

        //저장 버튼
        binding.saveBtn.setOnClickListener {
            saveFBDiaryData()
        }

        // 양식 선택 toggle
         binding.toggleBtn1.setOnClickListener {
            if (binding.toggleBtn1.isChecked) {
                // toggle_on
                binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_off)
                binding.title1.text = "칭찬할 점"
                binding.title2.text = "반성할 점"
                binding.title3.text = "느낀점"
                binding.content1Area.text = null
                binding.content2Area.text = null
                binding.content3Area.text = null
                sort = "ver2"

            }else{
                //toggle_off
                binding.toggleBtn1.setBackgroundResource(R.drawable.toggle_on)
                binding.title1.text = "좋았던 일"
                binding.title2.text = "안 좋았던 일"
                binding.title3.text = "나의 다짐"
                binding.content1Area.text = null
                binding.content2Area.text = null
                binding.content3Area.text = null
                sort = "ver1"
            }
        }
    }

    //파이어베이스 데이터 불러오기
    private fun getFBRandomQData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                randomQList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("randomQList", dataModel.toString())

                    val item = dataModel.value
                    randomQList.add(item!! as String)

                }
                Log.d("randomQListTest", randomQList.toString())

                // 랜덤 질문 리스트에서 랜덤 질문 하나 뽑아서 띄우기
                if(randomQList.size > 0){
                    val randomNum = Random().nextInt(randomQList.size)
                    Log.d("randomTest", "$randomNum")
                    binding.randomQ.text = randomQList[randomNum]
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("randomQListTest", "loadPost:onCancelled", databaseError.toException())
            }

        }
        FBRef.randomQuestionRef.addValueEventListener(postListener)
    }

    // 파이어베이스에 일기 데이터 저장
    private fun saveFBDiaryData(){
        val contents1 = binding.content1Area.text.toString()
        val contents2 = binding.content2Area.text.toString()
        val contents3 = binding.content3Area.text.toString()
        val r_question = binding.randomQ.text.toString()
        val r_contents = binding.randomA.text.toString()
        val time = FBAuth.getTimeDiary()
        val year = FBAuth.getYear()
        val month = FBAuth.getMonth()
        val day = FBAuth.getDay()
        val uid = FBAuth.getUid()

        val key = FBRef.diaryRef.push().key.toString()

        FBRef.diaryRef
            .child(key)
            .setValue(DiaryData(contents1, contents2, contents3, r_question, r_contents, sort, year, month, day, uid))

        Toast.makeText(this, "일기가 저장되었습니다.", Toast.LENGTH_LONG).show()

        finish()
    }

}