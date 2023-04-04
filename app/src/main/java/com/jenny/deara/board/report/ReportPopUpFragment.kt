package com.jenny.deara.board.report

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.*
import com.jenny.deara.board.BoardInsideActivity
import com.jenny.deara.board.comment.CommentPopupFragment
import com.jenny.deara.databinding.FragmentPopupBinding
import com.jenny.deara.databinding.FragmentReportPopupBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import java.util.ArrayList

class ReportPopUpFragment(val key: String) : DialogFragment() {

    private lateinit var binding: FragmentReportPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportPopupBinding.inflate(inflater, container, false)

        // 배경 제거하기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //게시글 신고
        binding.boardBtn.setOnClickListener {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        //작성자 신고
        binding.writerBtn.setOnClickListener{
            val intent = Intent(context, ReportAccountActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        //취소
        binding.closeBtn.setOnClickListener{
            dialog?.dismiss()
        }

        return binding.root
    }

//    //신고 한 번 만에 삭제처리
//    private fun report(key : String) {
//
//        val key : String = key
//
//        FBRef.reportRef
//            .child(key)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var dataModel = snapshot.getValue(ReportModel::class.java)
//                    val uid = FBAuth.getUid()
//                    //var value = dataModel?.report_count
//
//                    val intent = Intent(context, ReportActivity::class.java)
//                    intent.putExtra("key", key)
//                    startActivity(intent)

//                    if (value == null)
//                    {
//                        val intent = Intent(context, ReportActivity::class.java)
//                        intent.putExtra("key", key)
//                        startActivity(intent)
//                    }
//
//                    if (value != null) {
//                        val intent = Intent(context, ReportActivity::class.java)
//                        intent.putExtra("key", key)
//                        startActivity(intent)
//                        count = value
//                        when (count) {
//                            1 -> {
//                                user2 = uid
//                                if (user1 == user2)
//                                    Toast.makeText(
//                                        context,
//                                        "이미 신고한 게시글입니다",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                else {
//                                    val intent = Intent(context, ReportActivity::class.java)
//                                    intent.putExtra("key", key)
//                                    startActivity(intent)
//                                }
//                            }
//
//                            2 -> {
//                                user3 = uid
//                                if (user1 == user3 || user2 == user3)
//                                    Toast.makeText(
//                                        context,
//                                        "이미 신고한 게시글입니다",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                else {
//                                    val intent = Intent(context, ReportActivity::class.java)
//                                    intent.putExtra("key", key)
//                                    startActivity(intent)
//                                }
//                            }
//                            3 -> {
//                                user4 = uid
//                                if (user1 == user4 || user2 == user4 || user3 == user4)
//                                    Toast.makeText(
//                                        context,
//                                        "이미 신고한 게시글입니다",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                else {
//                                    val intent = Intent(context, ReportActivity::class.java)
//                                    intent.putExtra("key", key)
//                                    startActivity(intent)
//                                }
//                            }
//                            4 -> {
//                                user5 = uid
//                                if (user1 == user5 || user2 == user5 || user3 == user5 || user4 == user5)
//                                    Toast.makeText(
//                                        context,
//                                        "이미 신고한 게시글입니다",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                else {
//                                    val intent = Intent(context, ReportActivity::class.java)
//                                    intent.putExtra("key", key)
//                                    startActivity(intent)
//                                }
//                            }
//                        }

}