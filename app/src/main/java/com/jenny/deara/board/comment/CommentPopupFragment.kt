package com.jenny.deara.board.comment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jenny.deara.R
import com.jenny.deara.board.report.ReportActivity
import com.jenny.deara.board.report.ReportC_AccountActivity
import com.jenny.deara.board.report.ReportCommentActivity
import com.jenny.deara.databinding.FragmentBoardPopupBinding
import com.jenny.deara.databinding.FragmentCommentPopupBinding

class CommentPopupFragment(val key : String) : DialogFragment() {

    private lateinit var binding: FragmentCommentPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentPopupBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 댓글 신고
        binding.commentReport.setOnClickListener {
            val intent = Intent(context, ReportCommentActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        //  댓글 작성자 신고
        binding.commentWriterReport.setOnClickListener {
            val intent = Intent(context, ReportC_AccountActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        return binding.root
    }

}