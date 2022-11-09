package com.jenny.deara.board.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.databinding.CommentReplyListItemBinding

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
            //uid.text = FBAuth.getNick(item.uid)
        }
    }
}