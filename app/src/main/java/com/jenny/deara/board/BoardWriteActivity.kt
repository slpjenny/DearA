package com.jenny.deara.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.marginRight
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.diary.DiaryData
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var route: String = "write"
    private lateinit var key: String
    private lateinit var imageArea: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        imageArea = binding.imageArea

        route = intent.getStringExtra("route").toString()
        key = intent.getStringExtra("key").toString()

        if (route == "edit"){ // 수정 버튼으로 들어온 경우
            getBoardData(key)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.imageBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            //isImageUpload = true
        }

        binding.saveBtn.setOnClickListener {
            if(route == "edit"){  // 수정페이지
                saveFBBoardData(key)
            }else{ // 글 작성 페이지
                key = FBRef.diaryRef.push().key.toString()
                saveFBBoardData(key)
            }
        }
    }

    private fun saveFBBoardData(key: String){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()
        val uid = FBAuth.getUid()
        val time = FBAuth.getTimeBoard()

        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(title, content, uid, time))

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            // 이미지뷰 생성
            var imageView = ImageView(this)
            val imageLayoutParams = LinearLayout.LayoutParams(
                changeDP(100),
                changeDP(100),
            )
            imageLayoutParams.rightMargin = changeDP(10)
            imageView.layoutParams = imageLayoutParams
            imageView.setImageURI(data?.data)
            imageArea.addView(imageView)
            binding.imageScrollView.visibility = View.VISIBLE
        }
    }

    private fun changeDP(value : Int) : Int{
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }

}