package com.jenny.deara.board

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.storage
import com.jenny.deara.MyPageActivity
import com.jenny.deara.R
import com.jenny.deara.board.comment.CommentListAdapter
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.board.report.ReportActivity
import com.jenny.deara.board.report.ReportModel
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_inside.*
import kotlinx.android.synthetic.main.fragment_board_popup.*
import java.util.*

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    lateinit var CommentListAdapter: CommentListAdapter

    var count : Int = 0
    var commentList = mutableListOf<CommentModel>()
    var commentKeyList = mutableListOf<String>()
    var commentReplyOn : Boolean = false
    var dialogFlag : Boolean = false
    lateinit var getCommentKey : String

    var commentTest = mutableListOf<CommentModel>()
    var commentKeyListTest = mutableListOf<String>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        binding.linearLayout4.visibility = View.INVISIBLE

        val key = intent.getStringExtra("key")
        if (key != null) {
            getBoardData(key)
            getImageData(key)
            initRecycler(key)
            getCommentData(key)
        }

        //local
        //initRecycler("test")
        //getCommentData("test")

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.myPageBtn.setOnClickListener{
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.popupBtn.setOnClickListener {
            if (key != null) {
                BoardPopupFragment(key, commentKeyList).show(supportFragmentManager, "SampleDialog")
            }
        }

        binding.shingoBtn.setOnClickListener {
            if (key != null) {
                reportTwice(key)
            }
        }


        // 댓글 작성
        binding.commentBtn.setOnClickListener {
           if(!commentReplyOn){
               Log.d("commentInsert", "댓글을 작성")
               if (key != null) {
                   insertComment("null", key)
               }
           }
        }

//        Log.d("commentCount", CommentListAdapter.getAllItemCount().toString())
//        binding.commentNum.text = CommentListAdapter.getAllItemCount().toString()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(boardKey: String) {
        CommentListAdapter = CommentListAdapter(this, commentKeyList, boardKey)

        val rv : RecyclerView = binding.rvComment
        rv.adapter= CommentListAdapter

        CommentListAdapter.datas = commentList
        CommentListAdapter.notifyDataSetChanged()

        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            CommentListAdapter.setOnItemClickListener(object :
                CommentListAdapter.OnItemClickListener {
                @SuppressLint("ServiceCast", "ClickableViewAccessibility")
                override fun onItemClick(v: View, position: Int) {
                    if(!commentReplyOn){
                        v.findViewById<View>(R.id.Area1).setBackgroundColor(Color.parseColor("#EFF1FF"))
                        // 대댓글 작성하기
                        commentReplyOn = true
                        binding.commentArea.requestFocus()
                        imm.showSoftInput(binding.commentArea, InputMethodManager.SHOW_IMPLICIT)
                        binding.commentArea.hint = "답글을 입력해주세요"
                        getCommentKey = commentKeyList[position]

                        Log.d("TouchTest", "click comment : $commentReplyOn")
                        binding.main.setOnTouchListener { v, event ->
                            //popup()
                            if (commentReplyOn && !dialogFlag) {
                                dialogFlag = true
                                val mDialogView = Dialog(this@BoardInsideActivity)
                                mDialogView.setContentView(R.layout.comment_popup)
                                mDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                mDialogView.show()

                                val cancel = mDialogView.findViewById<View>(R.id.cancelBtn)
                                cancel.setOnClickListener {
                                    mDialogView.dismiss()
                                    dialogFlag = false
                                }

                                val noButton = mDialogView.findViewById<View>(R.id.delBtn)
                                noButton.setOnClickListener {
                                    commentReplyOn = false
                                    Log.d("TouchTest", "popup in $commentReplyOn")
                                    hideKeyboard()
                                    v.findViewById<View>(R.id.Area1)
                                        .setBackgroundColor(Color.parseColor("#00FF0000"))
                                    binding.commentArea.text = null
                                    binding.commentArea.hint = "댓글을 입력해주세요"
                                    mDialogView.dismiss()
                                    dialogFlag = false
                                }
                            }
                            Log.d("TouchTest", "popup$commentReplyOn")
                            false
                        }

                        // 답글은 여기서 작성
                        binding.commentBtn.setOnClickListener {
                            if (commentReplyOn) {
                                binding.commentArea.hint = "댓글을 입력해주세요"
                                v.findViewById<View>(R.id.Area1)
                                    .setBackgroundColor(Color.parseColor("#00FF0000"))
                                Log.d("commentInsert", "답글을 작성 : $getCommentKey")
                                insertComment(getCommentKey, boardKey)
                            }
                        }
                    }
                }
            })
    }

    // 이전 데이터 띄우기
    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                binding.titleArea.text = dataModel?.title
                binding.contentTextArea.text = dataModel?.content
                binding.sortArea.text = dataModel?.sort
                binding.boardTime.text = dataModel?.time
                binding.boardWriter.text = dataModel?.uid?.let { FBAuth.getNick(it) }
                if (dataModel != null) {
                    if (dataModel.uid == FBAuth.getUid()){
                        binding.popupBtn.visibility = View.VISIBLE
                    }
                    else if (dataModel.uid != FBAuth.getUid()){
                        binding.shingoBtn.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardInside", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    // 이미지 가져와서 띄우기
    private fun getImageData(key : String){
        val storage = Firebase.storage
        val listRef = storage.reference.child(key)

        listRef.listAll()
            .addOnSuccessListener { (items) ->

                //binding.imageArea.visibility = View.VISIBLE
                items.forEach { item ->
                    val getImage = ImageView(this)
                    val imageLayoutParams = LinearLayout.LayoutParams(changeDP(150), changeDP(150))
                    imageLayoutParams.setMargins(changeDP(3))
                    getImage.layoutParams = imageLayoutParams
                    getImage.setBackgroundResource(R.drawable.corner)
                    getImage.clipToOutline = true
                    binding.imageArea.addView(getImage)
                    item.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(this)
                                .load(task.result)
                                .override(changeDP(150),changeDP(150))
                                .transform(CenterCrop(), RoundedCorners( 10))
                                .into(getImage)

                            // 이미지 누르면 크게 보기
                            getImage.setOnClickListener {
                                val intent = Intent(this, BoardImageViewActivity::class.java)
                                intent.putExtra("url", task.result)
                                startActivity(intent)
                            }
                        } else {
                            Log.d("getImageLog", "task.Failure")
                        }
                    }
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                binding.imageArea.visibility = View.GONE
                Log.d("getImageLog", "Failure")
            }
    }

    private fun changeDP(value : Int) : Int{
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }

    // 댓글 작성하기
    private fun insertComment(parentKey: String, key: String){

        val viewType = if (parentKey == "null"){
            1
        }else{
            2
        }
        FBRef.commentRef
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getUid(),
                    FBAuth.getTimeBoard(),
                    parentKey,
                    key,
                    viewType
                )
            )

        if(commentReplyOn){
            Toast.makeText(this, "답글 입력 완료", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        }
        commentReplyOn = false
        binding.commentArea.setText("")

        // 화면 refresh
        finish() //인텐트 종료
        overridePendingTransition(0, 0) //인텐트 효과 없애기
        val intent = intent //인텐트
        startActivity(intent) //액티비티 열기
        overridePendingTransition(0, 0) //인텐트 효과 없애기
    }

    // 댓글 가져오기
    @SuppressLint("SetTextI18n")
    fun getCommentData(key : String){

        val commentCountList = mutableListOf<String>()

        // test data
//        commentList.clear()
//        commentKeyList.clear()
//
//        commentList.add(
//            CommentModel(
//                "댓글1",
//                "1",
//                "2020/01/01 11:11",
//                "null",
//                "boardKey",
//                1))
//        commentList.add(
//            CommentModel(
//                "대댓글1",
//                "2",
//                "2020/01/01 11:11",
//                "1",
//                "boardKey",
//                2))
//        commentList.add(
//            CommentModel(
//                "대댓글2",
//                "2",
//                "2020/01/01 11:11",
//                "1",
//                "boardKey",
//                1))
//        commentList.add(
//            CommentModel(
//                "댓글2",
//                "2",
//                "2020/01/01 11:11",
//                "null",
//                "boardKey",
//                1))
//        commentList.add(
//            CommentModel(
//                "댓글3",
//                "2",
//                "2020/01/01 11:11",
//                "null",
//                "boardKey",
//                1))
//        commentKeyList.add("1")
//        commentKeyList.add("2")
//        commentKeyList.add("3")
//        commentKeyList.add("4")
//        commentKeyList.add("5")

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                commentKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    if (item != null) {
                        if (item.boardKey == key)
                            if (item.parent == "null"){
                                commentList.add(item!!)
                                commentKeyList.add(dataModel.key.toString())
                            }else{ // 대댓글인 경우 리스트의 중간에 삽입하기
                                val arrayItem = item.parent
                                val index = commentKeyList.indexOf(arrayItem)

                                val count = Collections.frequency(commentCountList, arrayItem) + 1

                                commentList.add(index + count, item)
                                commentKeyList.add(index + count, dataModel.key.toString())

                                commentCountList.add(arrayItem)
                            }
                    }
                    Log.d("getCommentLog", "{${commentKeyList}}")

                    //getCommentReply(dataModel.key.toString()) //대댓글 리스트에 내용을 담는다.
                }
                CommentListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addListenerForSingleValueEvent(postListener)

        binding.commentNum.text = CommentListAdapter.itemCount.toString()
    }

    //중복 신고 막기
    private fun reportTwice(key : String) {

        val key : String = key

        FBRef.reportRef
            .child(key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var dataModel = snapshot.getValue(ReportModel::class.java)
                    val uid = FBAuth.getUid()
                    var value = dataModel?.report_count
                    var user1 = dataModel?.reporter_uid1
                    var user2 = dataModel?.reporter_uid2
                    var user3 = dataModel?.reporter_uid3
                    var user4 = dataModel?.reporter_uid4
                    var user5 = dataModel?.reporter_uid5

                    if (value == null)
                    {
                        val intent = Intent(this@BoardInsideActivity, ReportActivity::class.java)
                        intent.putExtra("key", key)
                        startActivity(intent)
                    }

                    if (value != null) {
                        count = value
                        when (count) {
                            1 -> {
                                user2 = uid
                                if (user1 == user2)
                                    Toast.makeText(
                                        this@BoardInsideActivity,
                                        "이미 신고한 게시글입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    val intent = Intent(this@BoardInsideActivity, ReportActivity::class.java)
                                    intent.putExtra("key", key)
                                    startActivity(intent)
                                }
                            }

                            2 -> {
                                user3 = uid
                                if (user1 == user3 || user2 == user3)
                                    Toast.makeText(
                                        this@BoardInsideActivity,
                                        "이미 신고한 게시글입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    val intent = Intent(this@BoardInsideActivity, ReportActivity::class.java)
                                    intent.putExtra("key", key)
                                    startActivity(intent)
                                }
                            }
                            3 -> {
                                user4 = uid
                                if (user1 == user4 || user2 == user4 || user3 == user4)
                                    Toast.makeText(
                                        this@BoardInsideActivity,
                                        "이미 신고한 게시글입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    val intent = Intent(this@BoardInsideActivity, ReportActivity::class.java)
                                    intent.putExtra("key", key)
                                    startActivity(intent)
                                }
                            }
                            4 -> {
                                user5 = uid
                                if (user1 == user5 || user2 == user5 || user3 == user5 || user4 == user5)
                                    Toast.makeText(
                                        this@BoardInsideActivity,
                                        "이미 신고한 게시글입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else {
                                    val intent = Intent(this@BoardInsideActivity, ReportActivity::class.java)
                                    intent.putExtra("key", key)
                                    startActivity(intent)
                                }
                            }
                        }
                    }


                }

            })
    }



    // 화면 밖 누르면 키보드 내리기
    open fun hideKeyboard() {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

}


