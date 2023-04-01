package com.jenny.deara.board.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.board.BoardListAdapter
import com.jenny.deara.board.BoardModel
import com.jenny.deara.board.comment.CommentListAdapter
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*

class ReportC_AccountActivity : AppCompatActivity() {

    private var binding: ActivityBoardInsideBinding? = null

    private var CommentListAdapter: CommentListAdapter? = null
    private var BoardListAdapter: BoardListAdapter? = null

    var boardList = mutableListOf<BoardModel>()
    var boardkeyList = mutableListOf<String>()
    var commentList = mutableListOf<CommentModel>()
    var commentKeyList = mutableListOf<String>()
    var commentBoardKeyList = mutableListOf<String>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_account)

        //댓글 key값 받아오기
        val key = intent.getStringExtra("key")
        Toast.makeText(this@ReportC_AccountActivity, "key = "+key, Toast.LENGTH_SHORT).show()

        if(key!=null)
        {
            getWriterData(key)
        }

        //취소버튼
        report_cancel.setOnClickListener{
            onBackPressed()
        }

        //뒤로가기
        back.setOnClickListener{
            onBackPressed()
        }

        radio_group.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.radio1 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio2 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio3 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio4 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio5 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio6 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio7 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio8 -> {
                    report_submit.isEnabled=true
                }
                R.id.radio9 -> {
                    report_submit.isEnabled=true
                }
            }
        }

        //확인버튼
        report_submit.setOnClickListener{

            //딸린 댓글
            for(i in commentKeyList)
            {
                FBRef.commentRef.child(i).removeValue()
            }

            // 게시자 댓글 목록 삭제
            for(i in commentBoardKeyList)
            {
                FBRef.commentRef.child(i).removeValue()
            }

            // 게시글 목록 삭제
            for(i in boardkeyList)
            {
                FBRef.boardRef.child(i).removeValue()
            }

            CommentDialog(key.toString()).show(supportFragmentManager, "CommentDialog")

        }

    }

    // 게시자 Uid
    private fun getWriterData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(CommentModel::class.java)

                if (dataModel != null) {
                    //신고당한 댓글주 uid 넘기기
                    Toast.makeText(this@ReportC_AccountActivity, "uid = "+dataModel.uid, Toast.LENGTH_SHORT).show()
                    getFBBoardData(dataModel.uid)
                    getCommentListData(dataModel.uid)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardInside", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    // 게시자 글 목록
    private fun getFBBoardData(writer : String) {

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardList.clear()
                boardkeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    Log.d("boardListTest", dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)

                    if (item != null) {
                        // item.uid=작성자 uid
                        // 신고당한 사람이 쓴 게시글 key 목록
                        if (item.uid == writer) {
                            boardList.add(item)
                            boardkeyList.add(dataModel.key.toString())
                        }
                    }
                }

                boardkeyList.reverse()
                boardList.reverse()
                BoardListAdapter?.notifyDataSetChanged()

                getCommentData(boardkeyList)
                Log.d("boardListTest", boardList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("boardListTest", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

    // 게시자 댓글 목록
    private fun getCommentListData(writer : String){
        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                commentBoardKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)

                    if (item != null) {
                        // 신고당한 게시자가 쓴 댓글 key 목록
                        if(item.uid == writer) {
                            commentList.add(item)
                            commentBoardKeyList.add(dataModel.key.toString())
                        }
                    }

                    Log.d("getCommentLog", "{${commentBoardKeyList}}")

                }
                BoardListAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)
    }

    // 게시글에 딸린 댓글 가져오기
    @SuppressLint("SetTextI18n")
    fun getCommentData(boardkeyList : MutableList<String>) {

        val commentCountList = mutableListOf<String>()

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                commentKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    if (item != null) {
                        for(i in boardkeyList)
                        {
                            if (item.boardKey == i)
                                if (item.parent == "null") {
                                    commentList.add(item)
                                    commentKeyList.add(dataModel.key.toString())

                                } else { // 대댓글인 경우 리스트의 중간에 삽입하기
                                    val arrayItem = item.parent
                                    val index = commentKeyList.indexOf(arrayItem)

                                    val count = Collections.frequency(commentCountList, arrayItem) + 1

                                    commentList.add(index + count, item)
                                    commentKeyList.add(index + count, dataModel.key.toString())
                                    commentCountList.add(arrayItem)
                                }
                        }

                    }
                    Log.d("getCommentLog", "{${commentKeyList}}")

                }
                CommentListAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addListenerForSingleValueEvent(postListener)

        binding?.commentNum?.text = CommentListAdapter?.itemCount.toString()
    }

}