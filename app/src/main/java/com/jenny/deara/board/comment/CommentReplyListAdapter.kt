package com.jenny.deara.board.comment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.databinding.CommentReplyListItemBinding
import com.jenny.deara.utils.FBRef

class CommentReplyListAdapter (private val datas: MutableList<CommentModel>) : RecyclerView.Adapter<CommentReplyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CommentReplyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: CommentReplyListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(private val binding:  CommentReplyListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentModel) {
            binding.commentContent.text = item.content
            binding.commentTime.text = item.time
            binding.commentWriter.text = "작성자"
//            binding.delBtn.setOnClickListener {
//                // popup
//                val mDialogView = Dialog(context)
//                mDialogView.setContentView(R.layout.comment_popup)
//                mDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//                mDialogView.show()
//
//                val cancel = mDialogView.findViewById<View>(R.id.cancelBtn)
//                cancel.setOnClickListener {
//                    mDialogView.dismiss()
//                }
//
//                val noButton = mDialogView.findViewById<View>(R.id.delBtn)
//                noButton.setOnClickListener {
//                    // 삭제버튼 클릭 이벤트
//                    FBRef.commentRef.child(boardKey).child(commentKeyList[position]).removeValue()
//                    Toast.makeText(getContext, "삭제완료", Toast.LENGTH_LONG).show()
//                    mDialogView.dismiss()
//                }
//            }
//            }
            //uid.text = FBAuth.getNick(item.uid)
        }
    }
}