package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.core.view.marginRight
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.image_list_item.*
import java.io.ByteArrayOutputStream
import kotlinx.android.synthetic.main.image_list_item.delBtn as delBtn1

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var route: String = "write"
    private lateinit var key: String
    private lateinit var imageTest : ImageView
    private var isImageUpload = false

    lateinit var ImageListAdapter : ImageListAdapter
    val imageList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        //image = ImageView(this@BoardWriteActivity)
        //imageArea = binding.imageArea

        //initRecycler()

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

//    @SuppressLint("NotifyDataSetChanged")
//    private fun initRecycler() {
//        ImageListAdapter = ImageListAdapter(this)
//
//        val rv : RecyclerView = binding.rvImage
//        rv.adapter= ImageListAdapter
//
//        ImageListAdapter.datas = imageList
//        ImageListAdapter.notifyDataSetChanged()
//    }

    private fun saveFBBoardData(key: String){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()
        val uid = FBAuth.getUid()
        val time = FBAuth.getTimeBoard()

        this.key = FBRef.boardRef.push().key.toString()

        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(title, content, uid, time))

        if(isImageUpload) {
            imageUpload(key)
        }

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

    private fun imageUpload(key : String){
        // Get the data from an ImageView as bytes

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")
        //var imageTest : ImageView

        for(i in imageList.indices){
            imageTest.setImageURI(imageList[i])
            imageTest.isDrawingCacheEnabled = true
            imageTest.buildDrawingCache()
            val bitmap = (imageTest.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            // 이미지뷰 생성
            val getImage = ImageView(this)
//            val imageLayoutParams = LinearLayout.LayoutParams(
//                changeDP(100),
//                changeDP(100),
//            )
//            imageLayoutParams.rightMargin = changeDP(10)
//            image.layoutParams = imageLayoutParams
            getImage.setImageURI(data?.data)
            //imageArea.addView(image)
            data?.data?.let { imageList.add(it) }
//            delBtn1.setOnClickListener {
//                imageList.removeAt(position)
//            }

            ImageListAdapter = ImageListAdapter(this, data)

            val rv : RecyclerView = binding.rvImage
            rv.adapter= ImageListAdapter
            rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            ImageListAdapter.datas = imageList
            ImageListAdapter.notifyDataSetChanged()

            //binding.rvImage.visibility = View.VISIBLE
        }
    }

}