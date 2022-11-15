package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_inside.*

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        val key = intent.getStringExtra("key")
        if (key != null) {
            getBoardData(key)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myPageBtn.setOnClickListener{
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.popupBtn.setOnClickListener {
            if (key != null) {
                BoardPopupFragment(key).show(supportFragmentManager, "SampleDialog")
            }
        }

        binding.shingo.setOnClickListener {
            count++
            if(count==3) shingo.isVisible=false
        }

    }

    // 이전 데이터 띄우기
    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                binding.titleArea.text = dataModel?.title
                binding.contentArea.text = dataModel?.content
                binding.boardTime.text = dataModel?.time
                //binding.boardWriter.text = dataModel?.uid?.let { FBAuth.getNick(it) }
                if (dataModel != null) {
                    if (dataModel.uid == FBAuth.getUid()){
                        binding.popupBtn.visibility = View.VISIBLE
                    }
                    else if (dataModel.uid != FBAuth.getUid()){
                        binding.shingo.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardInside", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}