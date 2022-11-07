package com.jenny.deara.board

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.storage
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
            delStorage(key)
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

    // 기기로 테스트 하기
    private fun delStorage(key: String){
//        for (i in 0 .. 10){
//            val imageRefer = Firebase.storage.reference.child(key).child("boardImage$i.png")
//
//            // Delete the file
//            imageRefer.delete().addOnSuccessListener {
//                Log.d("delLog", "Success -> boardImage$i.png")
//                // File deleted successfully
//            }.addOnFailureListener {
//                // Uh-oh, an error occurred!
//                Log.d("delLog", "Failure -> boardImage$i.png")
//            }
//        }

        val storage = Firebase.storage
        val listRef = storage.reference.child(key)  //폴더

        listRef.listAll()
            .addOnSuccessListener { (items) ->
                items.forEach { item ->
                    item.delete().addOnSuccessListener {
                        Log.d("delLog", "Success -> $item")
                    }.addOnFailureListener {
                        Log.d("delLog", "Failure -> $item")
                    }
                }
            }
            .addOnFailureListener {
                Log.d("delLog", "listAll() Fail")
            }


    }

}