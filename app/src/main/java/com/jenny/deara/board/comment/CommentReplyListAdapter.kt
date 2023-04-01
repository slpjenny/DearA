package com.jenny.deara.board.comment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.board.report.ReportAccountActivity
import com.jenny.deara.board.report.ReportC_AccountActivity
import com.jenny.deara.board.report.ReportCommentActivity
import com.jenny.deara.databinding.CommentReplyListItemBinding
import com.jenny.deara.utils.FBRef

class CommentReplyListAdapter (val context: Context,
                               private val datas: MutableList<CommentModel>,
                               private var commentReplyKeyList: MutableList<String>) : RecyclerView.Adapter<CommentReplyListAdapter.ViewHolder>() {

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
            //uid.text = FBAuth.getNick(item.uid)

            binding.delBtn.setOnClickListener {
                // popup
                val mDialogView = Dialog(context)
                mDialogView.setContentView(R.layout.comment_popup)
                mDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                mDialogView.show()

                val cancel = mDialogView.findViewById<View>(R.id.cancelBtn)
                cancel.setOnClickListener {
                    mDialogView.dismiss()
                }

                val noButton = mDialogView.findViewById<View>(R.id.delBtn)
                noButton.setOnClickListener {
                    // 삭제버튼 클릭 이벤트
                    FBRef.commentRef.child(commentReplyKeyList[position]).removeValue()
                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()
                }
            }

            binding.commentMenu.setOnClickListener {
                // popup
                val cDialogView = Dialog(context)
                cDialogView.setContentView(R.layout.fragment_comment_popup)
                cDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                cDialogView.show()

                val commentReport = cDialogView.findViewById<View>(R.id.commentReport)
                commentReport.setOnClickListener {
                    val intent = Intent(context, ReportCommentActivity::class.java)
                    intent.putExtra("key", commentReplyKeyList[position])
                    Toast.makeText(context, "key = "+commentReplyKeyList[position], Toast.LENGTH_SHORT).show()
                    context.startActivity(intent)
                    cDialogView.dismiss()
                }

                val commentWriterReport = cDialogView.findViewById<View>(R.id.commentWriterReport)
                commentWriterReport.setOnClickListener {
                    val intent = Intent(context, ReportC_AccountActivity::class.java)
                    intent.putExtra("key", commentReplyKeyList[position])
                    context.startActivity(intent)
                    cDialogView.dismiss()
                }
            }
        }
    }
}