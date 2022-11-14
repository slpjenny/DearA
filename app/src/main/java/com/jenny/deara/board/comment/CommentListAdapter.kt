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
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import kotlin.properties.Delegates

class CommentListAdapter(val context: Context, var commentKeyList: MutableList<String>) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    var datas = mutableListOf<CommentModel>()
    var reply by Delegates.notNull<Int>()
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  CommentListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position], commentKeyList[position])
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

            getCommentReply(commentKeyList[position])
            replyCount += size
            Log.d("sizeRe", "replyCount$replyCount")
            Log.d("sizeRe", "size$size")

            rv.apply {
                adapter = CommentReplyListAdapter(datasReply)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
                noButton.setOnClickListener { // 삭제버튼 클릭 이벤트
                    FBRef.commentRef.child(commentKeyList[position]).removeValue()
                    Toast.makeText(context, "삭제완료", Toast.LENGTH_LONG).show()
                    mDialogView.dismiss()
                }
            }

//            reply = rv.adapter?.itemCount!!
//            replyCount += reply

//            replyBtn.setOnClickListener {
//                comment.setBackgroundColor(Color.parseColor("#EFF1FF"))
//            }
        }
    }

    fun getAllItemCount(): Int = replyCount + itemCount

    private fun getCommentReply(commentKey: String) {
        // 파이어베이스에 대댓글 리스트 담기
//        val postListener = object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                datasReply.clear()
//
//                for (dataModel in dataSnapshot.children) {
//
//                    val item = dataModel.getValue(CommentModel::class.java)
//                    datasReply.add(item!!)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        FBRef.commentReplyRef.child(commentKey).addValueEventListener(postListener)

        datasReply.add(CommentModel("대댓글입니다.","uid","2022/11/07 21:28"))
        datasReply.add(CommentModel("두번째 대댓글입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다.","uid","2022/11/07 21:28"))
        datasReply.add(CommentModel("세번째 대댓글입니다.","uid","2022/11/07 21:28"))

        // 여기서 갯수를 리스트를 반환
        size = datasReply.size
    }
}