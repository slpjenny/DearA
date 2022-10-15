package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

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
}