package com.jenny.deara.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityAddRecordBinding
import com.jenny.deara.databinding.ActivityRmRecordBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class RmRecordActivity : AppCompatActivity() {

    val binding by lazy { ActivityRmRecordBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 이미 저장된 상세내역 파이어베이스에서 불러오기
        val key = intent.getStringExtra("key").toString()
        getRecordData(key)


        // 뒤로가기
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 변경된 내용으로 수정
        binding.saveBtn.setOnClickListener {
            editRecord(key)
        }

        // 아이템 삭제
        binding.removeBtn.setOnClickListener {
            removeRecord(key)
        }

    }


    private fun getRecordData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(RecordData::class.java)

                binding.reHospitalName.setText(dataModel?.hospitalName)
                binding.reDate.setText(dataModel?.date)
                binding.reTime.setText(dataModel?.time)
                binding.rePillName.setText(dataModel?.pillName)
                binding.reDosage.setText(dataModel?.dosage)
                binding.reMemo.setText(dataModel?.memo)
                binding.reSymptom.setText(dataModel?.symptom)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("recordEdit", "loadPost:onCancelled", databaseError.toException())
            }

        }

        FBRef.recordRef.child(key).addValueEventListener(postListener)

    }

    private fun editRecord(key : String){

        // 아이템 키값으로 구별
        FBRef.recordRef.child(key).setValue(
            RecordData(
                binding.reHospitalName.text.toString(),
                binding.reDate.text.toString(),
                binding.reTime.text.toString(),
                binding.rePillName.text.toString(),
                binding.reDosage.text.toString(),
                binding.reMemo.text.toString(),
                binding.reSymptom.text.toString(),
                FBAuth.getUid()
                )
        )

        Toast.makeText(this,"수정완료", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun removeRecord(key : String){
        FBRef.recordRef.child(key).removeValue()
        Toast.makeText(baseContext,"삭제가 완료되었습니다.",Toast.LENGTH_LONG).show()

        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}