package com.jenny.deara.board

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jenny.deara.MainActivity
import com.jenny.deara.databinding.FragmentBoardPopupBinding
import com.jenny.deara.utils.FBRef

class BoardPopupFragment(var key: String) : DialogFragment() {

    private lateinit var binding: FragmentBoardPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardPopupBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.editBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            intent.putExtra("route", "edit")
            intent.putExtra("key", key)
            startActivity(intent)
        }
        binding.delBtn.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("nav_board", "fifth")
            startActivity(intent)
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}