package com.jenny.deara.board

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.RoundedCorner
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_inside.*
import kotlinx.android.synthetic.main.fragment_board_popup.*
import kotlin.properties.Delegates

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    lateinit var CommentListAdapter: CommentListAdapter

    var commentList = mutableListOf<CommentModel>()
    var commentKeyList = mutableListOf<String>()
    var commentReplyOn : Boolean = false
    var dialogFlag : Boolean = false
    lateinit var getCommentKey : String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        val key = intent.getStringExtra("key")
        if (key != null) {
            getBoardData(key)
            getImageData(key)
            initRecycler(key)
            getCommentData(key)
            Log.d("commentkey", commentKeyList.toString())
        }

        //local
//        initRecycler()
//        getCommentData("test")

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


        // 댓글 작성
        binding.commentBtn.setOnClickListener {
            if (commentReplyOn){ // 대댓글 작성
                Log.d("commentInsert", "답글을 작성 : $getCommentKey")
                insertReComment(getCommentKey)
                commentReplyOn = false
            }else{ //댓글 작성
                Log.d("commentInsert", "댓글을 작성")
                if (key != null) {
                    Log.d("commentInsert", "댓글을 작성")
                    insertComment(key)
                }
            }
        }

        Log.d("commentCount", CommentListAdapter.getAllItemCount().toString())
        binding.commentNum.text = CommentListAdapter.getAllItemCount().toString()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(boardKey: String) {
        CommentListAdapter = CommentListAdapter(this, commentKeyList, boardKey)
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        CommentListAdapter.setOnItemClickListener(object: CommentListAdapter.OnItemClickListener{
            @SuppressLint("ServiceCast", "ClickableViewAccessibility")
            override fun onItemClick(v: View, position: Int) {
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
                    if (commentReplyOn && !dialogFlag){
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
                            binding.commentArea.text = null
                            binding.commentArea.hint = "댓글을 입력해주세요"
                            v.findViewById<View>(R.id.Area1).setBackgroundColor(Color.parseColor("#00FF0000"))
                            mDialogView.dismiss()
                            dialogFlag = false
                        }
                    }
                    Log.d("TouchTest", "popup$commentReplyOn")
                    false
                }

                // 텍스트 입력부분 이벤트 처리
                binding.commentArea.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (binding.commentArea.text.toString() == ""){
                            //view.findViewById<View>(R.id.Area1).setBackgroundColor(Color.parseColor("#EFF1FF"))
                            //binding.searchBtn.setColorFilter(Color.parseColor("#00ff0000))
                        }else{
                            //binding.searchBtn.setColorFilter(Color.parseColor("#B8B8F0"))
                        }
                    }
                })
            }
        })

        val rv : RecyclerView = binding.rvComment
        rv.adapter= CommentListAdapter

        CommentListAdapter.datas = commentList

//        val commentCount = CommentListAdapter.itemCount + CommentListAdapter.getReplyItemCount()
//        Log.w("commentCount", "CommentListAdapter.itemCount")
//        Log.w("commentCount", "CommentListAdapter.getReplyItemCount()")
//        Log.w("commentCount", "commentCount")
//        binding.commentNum.text = CommentListAdapter.getAllItemCount().toString()
//        Log.w("commentCountAll", CommentListAdapter.getAllItemCount().toString())
        CommentListAdapter.notifyDataSetChanged()
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
                //binding.boardWriter.text = dataModel?.uid?.let { FBAuth.getNick(it) }
                if (dataModel != null) {
                    if (dataModel.uid == FBAuth.getUid()){
                        binding.popupBtn.visibility = View.VISIBLE
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

    private fun insertComment(key: String){
        // comment
        //   - BoardKey
        //        - CommentKey
        //            - CommentData
        //            - CommentData
        //            - CommentData
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getUid(),
                    FBAuth.getTimeBoard()
                )
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }

    // 답글
    private fun insertReComment(key: String){
        // commentReply
        //   - CommentKey
        //        - reCommentKey
        //            - CommentData
        //            - CommentData
        //            - CommentData
        FBRef.commentReplyRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getUid(),
                    FBAuth.getTimeBoard()
                )
            )

        Toast.makeText(this, "답글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }

    // 댓글 가져오기
    @SuppressLint("SetTextI18n")
    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                commentKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                    commentKeyList.add(dataModel.key.toString())
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
        FBRef.commentRef.child(key).addValueEventListener(postListener)

        //test data//
//        commentList.add(CommentModel("댓글입니다.","uid","2022/11/07 21:28"))
//        commentList.add(CommentModel("두번째 댓글입니다.","uid","2022/11/07 21:28"))
//        commentList.add(CommentModel("세번째 댓글입니다.","uid","2022/11/07 21:28"))
//        commentList.add(CommentModel("네번째 댓글입니다.","uid","2022/11/07 21:28"))
//
//        commentKeyList.add("1")
//        commentKeyList.add("2")
//        commentKeyList.add("3")
//        commentKeyList.add("4")

        //binding.commentNum.text = CommentListAdapter.itemCount.toString() + CommentListAdapter.getReplyItemCount()
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


