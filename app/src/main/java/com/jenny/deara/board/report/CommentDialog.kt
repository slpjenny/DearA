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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jenny.deara.MainActivity
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.databinding.FragmentReportConfirmPopupBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class CommentDialog(key: String) : DialogFragment() {

    private var _binding: FragmentReportConfirmPopupBinding? = null

    private val binding get() = _binding!!
    val database = Firebase.database

    val key : String = key

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReportConfirmPopupBinding.inflate(inflater, container, false)

        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // dialog 밖 누르면 창 없어지지 않게
        dialog?.setCanceledOnTouchOutside(false)

        // 확인버튼 (신고 완료)
        binding.confirm.setOnClickListener {

            // 댓글 삭제
            FBRef.commentRef.child(key).removeValue()
            Toast.makeText(context, "신고 접수가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT)
                .show() // 없앨 코드
            dialog?.dismiss()

            // 커뮤니티 목록으로
            changeFragment()

        }

        return view
    }

    private fun changeFragment()
    {

        var intent = Intent(context, MainActivity::class.java)
        intent.putExtra("nav", "fifth")
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}