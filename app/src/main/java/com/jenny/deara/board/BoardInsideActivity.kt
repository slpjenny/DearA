package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
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
import com.jenny.deara.board.comment.CommentReplyListAdapter
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_board_inside.*

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    lateinit var CommentListAdapter: CommentListAdapter

    var commentList = mutableListOf<CommentModel>()
    var commentKeyList = mutableListOf<String>()
    //var commentReplyList = mutableListOf<CommentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        val key = intent.getStringExtra("key")
        if (key != null) {
            getBoardData(key)
            getImageData(key)
            initRecycler()
            //getCommentData(key)
        }

        initRecycler()
        getCommentData("test")

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

        binding.commentBtn.setOnClickListener {
            if (key != null) {
                insertComment(key)
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        CommentListAdapter = CommentListAdapter(this, commentKeyList)

        val rv : RecyclerView = binding.rvComment
        rv.adapter= CommentListAdapter

        CommentListAdapter.datas = commentList
        var commentCount = CommentListAdapter.itemCount + CommentListAdapter.getReplyItemCount()
        binding.commentNum.text = commentCount.toString()
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

                val getImage = ImageView(this)
                val imageLayoutParams = LinearLayout.LayoutParams(changeDP(150), changeDP(150))
                imageLayoutParams.setMargins(changeDP(3))
                getImage.layoutParams = imageLayoutParams
                getImage.setBackgroundResource(R.drawable.corner)
                getImage.clipToOutline = true
                binding.imageArea.addView(imageArea)
                items.forEach { item ->
                    item.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(this)
                                .load(task.result)
                                .into(getImage)
                        } else {
                            Log.d("getImageLog", "task.Failure")
                        }
                    }
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                binding.imageArea.isVisible = false
                Log.d("getImageLog", "Failure")
            }
    }

    private fun changeDP(value : Int) : Int{
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }

    private fun insertComment(key: String){
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

    // 댓글 가져오기
    @SuppressLint("SetTextI18n")
    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                    commentKeyList.add(dataModel.key.toString())
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
        commentList.add(CommentModel("댓글입니다.","uid","2022/11/07 21:28"))
        commentList.add(CommentModel("두번째 댓글입니다.","uid","2022/11/07 21:28"))
        commentList.add(CommentModel("세번째 댓글입니다.","uid","2022/11/07 21:28"))
        commentList.add(CommentModel("네번째 댓글입니다.","uid","2022/11/07 21:28"))

        //binding.commentNum.text = CommentListAdapter.itemCount.toString() + CommentListAdapter.getReplyItemCount()
    }
}