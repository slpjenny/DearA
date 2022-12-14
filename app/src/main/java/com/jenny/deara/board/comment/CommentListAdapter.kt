package com.jenny.deara.board.comment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef


class CommentListAdapter(val context: Context,
                         var commentKeyList: MutableList<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas = mutableListOf<CommentModel>()

    // 커스텀 리스너
    interface OnItemClickListener{
        fun onItemClick(v: View, position : Int)
    }

    // 리스너 객체 참조를 저장하는 변수
    private lateinit var mOnItemClickListener: OnItemClickListener

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int {
        return datas[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
//        return ViewHolder(view)
//
        val view: View
        return when(viewType) {
            multi_type1 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.comment_list_item,
                    parent,
                    false
                )
                ViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.comment_reply_list_item,
                    parent,
                    false
                )
                ReplyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        when(datas[position].type) {
            multi_type1 -> {
                (holder as ViewHolder).bind(datas[position], commentKeyList[position])
                holder.setIsRecyclable(false) // 리사이클러뷰 재사용 막기
            }
            else -> {
                (holder as ReplyViewHolder).bind(datas[position], commentKeyList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)
        private val time: TextView = itemView.findViewById(R.id.commentTime)
        private val uid: TextView = itemView.findViewById(R.id.commentWriter)
        private val delBtn: TextView = itemView.findViewById(R.id.delBtn)

        init{
            view.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(view, pos)
                }
            }
        }

        fun bind(item: CommentModel, s: String) {
            content.text = item.content
            time.text = item.time
            uid.text = FBAuth.getNick(item.uid)

            if(item.uid !== FBAuth.getUid()){
                delBtn.visibility = View.INVISIBLE
            }
            if(item.uid == FBAuth.getUid()){
                delBtn.visibility = View.VISIBLE
            }

            //댓글 삭제 하기
            delBtn.setOnClickListener {
                // popup
                val mDialogView = Dialog(context)
                mDialogView.setContentView(R.layout.comment_popup_del)
                mDialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                mDialogView.show()

                val cancel = mDialogView.findViewById<View>(R.id.cancelBtn)
                cancel.setOnClickListener {
                    mDialogView.dismiss()
                }

                val noButton = mDialogView.findViewById<View>(R.id.delBtn)
                noButton.setOnClickListener {
                    // 삭제버튼 클릭 이벤트
                    FBRef.commentRef.child(commentKeyList[position]).removeValue()
                    // 답글 삭제
                    for (i in 0 until datas.size){
                        if (datas[i].parent == commentKeyList[position]){
                            FBRef.commentRef.child(commentKeyList[i]).removeValue()
                        }
                    }

                    // Activity refresh
                    val intent = (context as Activity).intent
                    context.finish() //현재 액티비티 종료 실시
                    context.overridePendingTransition(0, 0) //효과 없애기
                    context.startActivity(intent) //현재 액티비티 재실행 실시
                    context.overridePendingTransition(0, 0) //효과 없애기

                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()
                }
            }
        }
    }

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)
        private val time: TextView = itemView.findViewById(R.id.commentTime)
        private val uid: TextView = itemView.findViewById(R.id.commentWriter)
        private val delBtn: TextView = itemView.findViewById(R.id.delBtn)

//        init{
//            view.setOnClickListener {
//                val pos = adapterPosition
//                if(pos != RecyclerView.NO_POSITION && mOnItemClickListener != null){
//                    mOnItemClickListener.onItemClick(view, pos)
//                }
//            }
//        }

        fun bind(item: CommentModel, s: String) {
            content.text = item.content
            time.text = item.time
            //uid.text = "yet"
            uid.text = FBAuth.getNick(item.uid)

            if(item.uid !== FBAuth.getUid()){
                delBtn.visibility = View.INVISIBLE
            }
            if(item.uid == FBAuth.getUid()){
                delBtn.visibility = View.VISIBLE
            }

            //댓글 삭제 하기
            delBtn.setOnClickListener {
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
                    FBRef.commentRef.child(commentKeyList[position]).removeValue()

                    // Activity refresh
                    val intent = (context as Activity).intent
                    context.finish() //현재 액티비티 종료 실시
                    context.overridePendingTransition(0, 0) //효과 없애기
                    context.startActivity(intent) //현재 액티비티 재실행 실시
                    context.overridePendingTransition(0, 0) //효과 없애기

                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()
                }
            }
        }
    }
}