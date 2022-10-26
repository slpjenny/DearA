package com.jenny.deara.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityAddRecordBinding
import com.jenny.deara.databinding.ActivitySignInBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.diary.DiaryListAdapter
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class AddRecordActivity : AppCompatActivity() {

    val binding by lazy { ActivityAddRecordBinding.inflate(layoutInflater) }

    lateinit var RecordListAdapter: RecordListAdapter
    val recordList = mutableListOf<RecordData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 진료기록 저장
        binding.saveBtn.setOnClickListener {
            saveFBDiaryData()
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


    }

    // 파이어베이스에 진료 기록 데이터 저장
    private fun saveFBDiaryData(){
        // 병원명,복용약,진료 메모 등 한꺼번에 객체로 저장해야함
        // 리싸이클러뷰로 불러올때에는 객체에서 특정 항목만 데이터리스트에 추가해야함!!
        val hospitalName = binding.hospitalName.text.toString()
        val date = binding.date.text.toString()
        val time = binding.time.text.toString()
        val pillName = binding.pillName.text.toString()
        val dosage = binding.dosage.text.toString()
        val memo = binding.memo.text.toString()
        val symptom = binding.symptom.text.toString()
        val uid = FBAuth.getUid()

        val key = FBRef.recordRef.push().key.toString()

        // 진료 기록 객체 형태로 저장
        FBRef.recordRef
            .child(key)
            .setValue(RecordData(hospitalName,date,time,pillName,dosage,memo,symptom,uid))

        Toast.makeText(this, "진료 기록이 저장되었습니다.", Toast.LENGTH_LONG).show()

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}