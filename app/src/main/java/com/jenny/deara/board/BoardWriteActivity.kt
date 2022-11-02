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
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.core.view.marginRight
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.image_list_item.*
import kotlinx.android.synthetic.main.item_spinner.*
import java.io.ByteArrayOutputStream
import kotlinx.android.synthetic.main.image_list_item.delBtn as delBtn1

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var route: String = "write"
    private lateinit var key: String
    private var isImageUpload = false
    private var sort: String = "자유"

    lateinit var ImageListAdapter : ImageListAdapter
    val imageList = mutableListOf<Uri>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        //image = ImageView(this@BoardWriteActivity)
        //imageArea = binding.imageArea

        val items = resources.getStringArray(R.array.my_array)
        val spinnerAdapter = ArrayAdapter(this, R.layout.item_spinner, R.id.spinnerItemStyle, items)

        route = intent.getStringExtra("route").toString()
        key = intent.getStringExtra("key").toString()
        binding.spinner.adapter = spinnerAdapter

        //spinner.setSelection(2) // 2번째 포지션으로 이동합니다.

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                sort = when(position) {
                    0   ->  { //자유
                        "자유"
                    }
                    1   ->  { //질문
                        "질문"
                    }
                    else -> { //정보
                        "정보"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        if (route == "edit"){ // 수정 버튼으로 들어온 경우
            getBoardData(key)
            //getImageData(key)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.imageBtn.setOnClickListener {
            if (imageList.size >= 10){
                Toast.makeText(this, "사진은 최대 10개까지 업로드할 수 있습니다.", Toast.LENGTH_LONG).show()
            }else{
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
                isImageUpload = true
            }
        }

        binding.saveBtn.setOnClickListener {
            if(route == "edit"){  // 수정페이지
                saveFBBoardData(key)
            }else{ // 글 작성 페이지
                key = FBRef.diaryRef.push().key.toString()
                saveFBBoardData(key)
            }
        }

        binding.imageCount.text = imageList.size.toString() + "/10"
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
            .setValue(BoardModel(title, content, uid, time, sort))

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
                if (dataModel?.sort == "질문"){
                    spinner.setSelection(2)
                }
                if (dataModel?.sort == "정보"){
                    spinner.setSelection(3)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardEdit", "loadPost:onCancelled", databaseError.toException())
            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val imageRefer = Firebase.storage.reference.child(key)

        imageRefer.downloadUrl.addOnSuccessListener {
            imageList.add(it)
        }.addOnFailureListener {
            // Handle any errors
        }
        initRecycler()


    }

    private fun imageUpload(key : String){
        // Get the data from an ImageView as bytes

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key).child("imageTest.png")
        val imageTest = ImageView(this)

        //집가서 폰 연결해서 테스트하고 수정하기
        for(i in imageList.indices){
            imageTest.setImageURI(imageList[i])
            Log.d("uploadLog",i.toString() + imageList[i].toString())
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
            //val getImage = ImageView(this)
//            val imageLayoutParams = LinearLayout.LayoutParams(
//                changeDP(100),
//                changeDP(100),
//            )
//            imageLayoutParams.rightMargin = changeDP(10)
//            image.layoutParams = imageLayoutParams
            //getImage.setImageURI(data?.data)
            //imageArea.addView(image)
            if (imageList.size >= 10){
                Toast.makeText(this, "사진은 최대 10개까지 업로드할 수 있습니다.", Toast.LENGTH_LONG).show()
            }else{
                val getImage = ImageView(this)
                getImage.setImageURI(data?.data)
                data?.data?.let { imageList.add(it) }
                binding.imageCount.text = imageList.size.toString() + "/10"
            }
            //data?.data?.let { imageList.add(it) }
//            delBtn1.setOnClickListener {
//                imageList.removeAt(position)
//            }
            initRecycler()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(){
        ImageListAdapter = ImageListAdapter(this)

        val rv : RecyclerView = binding.rvImage
        rv.adapter= ImageListAdapter
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        ImageListAdapter.datas = imageList
        ImageListAdapter.notifyDataSetChanged()
    }

}