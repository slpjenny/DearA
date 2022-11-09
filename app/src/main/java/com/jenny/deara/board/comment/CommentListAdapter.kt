package com.jenny.deara.board.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.board.BoardInsideActivity
import com.jenny.deara.board.BoardListAdapter
import com.jenny.deara.board.BoardModel
import com.jenny.deara.utils.FBAuth

class CommentListAdapter(val context: Context, val datasReply :MutableList<CommentModel>) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    var datas = mutableListOf<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  CommentListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)
        private val time: TextView = itemView.findViewById(R.id.commentTime)
        private val uid: TextView = itemView.findViewById(R.id.commentWriter)
        private val rv: RecyclerView = itemView.findViewById(R.id.rvCommentReply)

        fun bind(item: CommentModel) {
            content.text = item.content
            time.text = item.time
            uid.text = "yet"
            //uid.text = FBAuth.getNick(item.uid)

            rv.apply {
                adapter = CommentReplyListAdapter(datasReply)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}