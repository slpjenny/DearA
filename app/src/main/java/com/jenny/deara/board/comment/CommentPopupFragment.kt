package com.jenny.deara.board.comment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jenny.deara.R
import com.jenny.deara.databinding.FragmentBoardPopupBinding
import com.jenny.deara.databinding.FragmentCommentPopupBinding

class CommentPopupFragment : DialogFragment() {

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

        }

        // 댓글 작성자 신고
        binding.commentWriterReport.setOnClickListener {

        }

        return binding.root
    }

}