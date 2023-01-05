package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.storage
import com.jenny.deara.R
import com.jenny.deara.databinding.ActivityBoardWriteBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_write.*
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var route: String = "write"
    private lateinit var key: String
    private var sort: String = "자유"

    private lateinit var ImageListAdapter : ImageListAdapter
    private val imageList = mutableListOf<Uri>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        initRecycler()

        binding.root.setOnClickListener {
            hideKeyboard()
        }

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
            getImageData(key)
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
                //isImageUpload = true
            }
        }

        binding.saveBtn.setOnClickListener {
            if(binding.titleArea.text.toString() == ""){
                Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_LONG).show()
                Log.d("nullCheck", "제목을 입력하세요")
            }else if(binding.contentArea.text.toString() == ""){
                Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_LONG).show()
                Log.d("nullCheck", "내용을 입력하세요")
            }else{
                if(route == "edit"){  // 수정페이지
                    saveFBBoardData(key)
                }else{ // 글 작성 페이지
                    key = FBRef.diaryRef.push().key.toString()
                    saveFBBoardData(key)
                }
            }
        }

        //binding.imageCount.text = imageList.size.toString() + "/10"
    }

    private fun saveFBBoardData(key: String){
        val title = binding.titleArea.text.toString()
        val content = binding.contentArea.text.toString()
        val uid = FBAuth.getUid()
        val time = FBAuth.getTimeBoard()

        this.key = FBRef.boardRef.push().key.toString()

        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(title, content, uid, time, sort))

        if(imageList.size > 0){
            Thread {
                imageUpload(key)
            }.start()
        }

        val intent = Intent(this, BoardInsideActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
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

    // 이전 이미지들 띄우기
    private fun getImageData(key : String){
        val storage = Firebase.storage
        val listRef = storage.reference.child(key)

        // You'll need to import com.google.firebase.storage.ktx.component1 and
        // com.google.firebase.storage.ktx.component2
        listRef.listAll()
            .addOnSuccessListener { (items) ->
                items.forEach { item ->
                    item.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                        if(task.isSuccessful){
                            imageList.add(task.result)
                            Log.d("getImageLog", task.result.toString())
                            Log.d("getImageLog", imageList.toString())
                            initRecycler()
                        }else{
                            Log.d("getImageLog", "task.Failure")
                        }
                    })
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                Log.d("getImageLog", "Failure")
            }
        Log.d("getImageLog", "final length : $imageList")
    }

    private fun imageUpload(key : String){
        // Get the data from an ImageView as bytes
        //delStorage(key)

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key)
        val imageTest = ImageView(this)
        lateinit var bitmap : Bitmap

        for(i in imageList.indices){
            imageTest.setImageURI(imageList[i])
            imageTest.isDrawingCacheEnabled = true
            imageTest.buildDrawingCache()
            bitmap = if (imageList[i].toString().lowercase().contains("https://")){
                getImageBitmap(imageList[i])
            }else{
                (imageTest.drawable as BitmapDrawable).bitmap
            }
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = mountainsRef.child("boardImage$i.png").putBytes(data)
            uploadTask.addOnFailureListener {
                Log.d("uploadLog","Failure -> " + i.toString() + imageList[i].toString())
            }.addOnSuccessListener { taskSnapshot ->
                Log.d("uploadLog","Success!(KEY:$key) imageNum -> " + i.toString() + " // URI ->"+imageList[i].toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100) {
            if (imageList.size >= 10){
                Toast.makeText(this, "사진은 최대 10개까지 업로드할 수 있습니다.", Toast.LENGTH_LONG).show()
            }else{
                data?.data?.let { imageList.add(it) }
                binding.imageCount.text = imageList.size.toString() + "/10"
            }
            ImageListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun initRecycler(){
        ImageListAdapter = ImageListAdapter(this)

        val rv : RecyclerView = binding.rvImage
        rv.adapter= ImageListAdapter
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        ImageListAdapter.datas = imageList
        ImageListAdapter.notifyDataSetChanged()
        binding.imageCount.text = ImageListAdapter.itemCount.toString() +"/10"
    }

    private fun getImageBitmap(url: Uri): Bitmap {
        lateinit var bm: Bitmap
        try {
            val aURL = URL(url.toString())
            val conn: URLConnection = aURL.openConnection()
            conn.connect()
            val `is`: InputStream = conn.getInputStream()
            val bis = BufferedInputStream(`is`)
            bm = BitmapFactory.decodeStream(bis)
            bis.close()
            `is`.close()
        } catch (e: IOException) {
            Log.e("BiteMap", "Error getting bitmap", e)
        }
        return bm
    }

    private fun hideKeyboard() {
        if(this != null && this.currentFocus != null) {
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}