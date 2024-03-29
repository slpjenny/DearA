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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.databinding.FragmentReportCommentBinding
import com.jenny.deara.databinding.FragmentReportPopupBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class ReportCommentFragment(key: String) : DialogFragment() {

    private lateinit var binding: FragmentReportCommentBinding

    val key : String = key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportCommentBinding.inflate(inflater, container, false)

        // 배경 제거하기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //게시글 신고
        binding.boardBtn.setOnClickListener {
            val intent = Intent(context, ReportCommentActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        //작성자 신고
        binding.writerBtn.setOnClickListener{
            val intent = Intent(context, ReportAccountActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        return binding.root
    }


}