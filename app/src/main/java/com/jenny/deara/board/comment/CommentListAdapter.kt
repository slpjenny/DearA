package com.jenny.deara.board.comment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.utils.FBRef
import kotlin.properties.Delegates

class CommentListAdapter(val context: Context, var commentKeyList: MutableList<String>) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    var datas = mutableListOf<CommentModel>()
    var reply by Delegates.notNull<Int>()
    var replyCount : Int = 0
    var datasReply = mutableListOf<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  CommentListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position], commentKeyList[position])
    }

    fun getReplyItemCount(): Int = replyCount

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)
        private val time: TextView = itemView.findViewById(R.id.commentTime)
        private val uid: TextView = itemView.findViewById(R.id.commentWriter)
        public val rv: RecyclerView = itemView.findViewById(R.id.rvCommentReply)

        fun bind(item: CommentModel, s: String) {
            content.text = item.content
            time.text = item.time
            uid.text = "yet"
            //uid.text = FBAuth.getNick(item.uid)

            getCommentReply(commentKeyList[position])

            rv.apply {
                adapter = CommentReplyListAdapter(datasReply)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            reply = rv.adapter?.itemCount!!
            replyCount += reply
        }
    }

    private fun getCommentReply(commentKey: String) {
        // 파이어베이스에 대댓글 리스트 담기
        val postListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                datasReply.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    datasReply.add(item!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("getCommentData", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentReplyRef.child(commentKey).addValueEventListener(postListener)

//        datasReply.add(CommentModel("대댓글입니다.","uid","2022/11/07 21:28"))
//        datasReply.add(CommentModel("두번째 대댓글입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다. 엄청엄청 긴 댓글 입니다.","uid","2022/11/07 21:28"))
//        datasReply.add(CommentModel("세번째 대댓글입니다.","uid","2022/11/07 21:28"))

        //return datasReply.size
        // 여기서 갯수를 리스트를 반환
    }
}