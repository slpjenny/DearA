package com.jenny.deara.board.comment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.board.BoardInsideActivity
import com.jenny.deara.board.BoardModel
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlin.properties.Delegates

class CommentListAdapter(val context: Context,
                         var commentKeyList: MutableList<String>,
                         val boardKey: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas = mutableListOf<CommentModel>()
    var replyCount : Int = 0
    var datasReply = mutableListOf<CommentModel>()
    var size : Int = 0

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
//        return ViewHolder(view)
//
        var view: View
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
//        if ( datas== "myComment"){ // 댓글로 연결
//            view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
//            return CommentViewHolder(view)
//        }else{
//            view = LayoutInflater.from(context).inflate(R.layout.board_list_item, parent,false)
//            return ViewHolder(view)
//        }
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        //holder.bind(datas[position], commentKeyList[position])

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
        public val rv: RecyclerView = itemView.findViewById(R.id.rvCommentReply)
        private val replyBtn: TextView = itemView.findViewById(R.id.replyBtn)
        private val comment: View = itemView.findViewById(R.id.boardComment)
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
            uid.text = "yet"
            //uid.text = FBAuth.getNick(item.uid)

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
                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()

//                    val delList = mutableListOf<String>()
//                    val postListener = object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                            val dataModel = dataSnapshot.getValue(BoardModel::class.java)
//                            if (item.parent == commentKeyList[position]){
//                                delList.add()
//                            }
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                            Log.w("boardTitleFromComment", "loadPost:onCancelled", databaseError.toException())
//                        }
//                    }
//                    FBRef.commentRef.child(commentKeyList[position]).addValueEventListener(postListener)
                }
            }
        }
    }

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)
        private val time: TextView = itemView.findViewById(R.id.commentTime)
        private val uid: TextView = itemView.findViewById(R.id.commentWriter)
        private val replyBtn: TextView = itemView.findViewById(R.id.replyBtn)
        private val comment: View = itemView.findViewById(R.id.boardComment)
        private val delBtn: TextView = itemView.findViewById(R.id.delBtn)
        private val reply: ImageView = itemView.findViewById(R.id.reply)

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
            uid.text = "yet"
            //uid.text = FBAuth.getNick(item.uid)

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
                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()
                }
            }
        }
    }
}