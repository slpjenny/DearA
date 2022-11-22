package com.jenny.deara.board.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class MyCommentListAdapter(val context: Context): RecyclerView.Adapter<MyCommentListAdapter.ViewHolder>(){

    var datas = mutableListOf<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_my_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val comment: TextView = itemView.findViewById(R.id.contentArea)
        private val time: TextView = itemView.findViewById(R.id.timeArea)

        fun bind(item: CommentModel) {
            comment.text = item.content
            time.text = item.time

            itemView.setOnClickListener {
                // 내부페이지로 이동
            }
        }
    }
}