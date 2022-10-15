package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var route: String = "write"
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        route = intent.getStringExtra("route").toString()
        key = intent.getStringExtra("key").toString()

        if (route == "edit"){ // 수정 버튼으로 들어온 경우
            getBoardData(key)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            saveFBBoardData()
        }
    }

    private fun saveFBBoardData(){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()
        val uid = FBAuth.getUid()
        val time = FBAuth.getTime()

        val key = FBRef.boardRef.push().key.toString()

        FBRef.boardRef
            .child(key)
            .setValue(DiaryData(title, content, uid, time))

        finish()
    }

    // 이전 데이터 띄우기
    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardEdit", "loadPost:onCancelled", databaseError.toException())
            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}