package com.jenny.deara.board.report

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.board.comment.CommentListAdapter
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.databinding.ActivityBoardInsideBinding
import com.jenny.deara.databinding.ActivityReportBinding
import com.jenny.deara.utils.FBRef
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*

class ReportActivity : AppCompatActivity() {

    private var CommentListAdapter: CommentListAdapter? = null

    var commentList = mutableListOf<CommentModel>()
    var commentKeyList = mutableListOf<String>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //key값 받아오기
        val key = intent.getStringExtra("key")

        if(key!=null)
        {
            getCommentData(key)
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
                    radio1_1.isVisible=true
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio2 -> {
                    report_submit.isEnabled=true
                    radio2_2.isVisible=true
                    radio1_1.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio3 -> {
                    report_submit.isEnabled=true
                    radio3_3.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio4 -> {
                    report_submit.isEnabled=true
                    radio4_4.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio5 -> {
                    report_submit.isEnabled=true
                    radio5_5.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio6_6.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio6 -> {
                    report_submit.isEnabled=true
                    radio6_6.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    edittext.isVisible=false
                }
                R.id.radio7 -> {
                    report_submit.isEnabled=true
                    edittext.isVisible=true
                    radio1_1.isVisible=false
                    radio2_2.isVisible=false
                    radio3_3.isVisible=false
                    radio4_4.isVisible=false
                    radio5_5.isVisible=false
                    radio6_6.isVisible=false
                }
            }
        }

        //확인버튼
        report_submit.setOnClickListener{
            CustomDialog(key.toString(), commentKeyList).show(supportFragmentManager, "FragmentReportPopup")

        }

    }

    // 댓글 가져오기
    @SuppressLint("SetTextI18n")
    fun getCommentData(key: String) {

        val commentCountList = mutableListOf<String>()

        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                commentKeyList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    if (item != null) {
                        if (item.boardKey == key)
                            if (item.parent == "null") {
                                commentList.add(item!!)
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
                    Log.d("getCommentLog", "{${commentKeyList}}")

                    //getCommentReply(dataModel.key.toString()) //대댓글 리스트에 내용을 담는다.
                }
                CommentListAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.addListenerForSingleValueEvent(postListener)

    }


}