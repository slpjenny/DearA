package com.jenny.deara.board.comment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.board.BoardInsideActivity
import com.jenny.deara.board.BoardModel
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef

class MyCommentListAdapter(val context: Context): RecyclerView.Adapter<MyCommentListAdapter.ViewHolder>(){

    var datas = mutableListOf<CommentModel>()
    var commentBoardKeyList = mutableListOf<String>()

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
        private val title: TextView = itemView.findViewById(R.id.titleArea)

        fun bind(item: CommentModel) {
            comment.text = item.content
            time.text = item.time
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    title.text = dataModel?.title.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("boardTitleFromComment", "loadPost:onCancelled", databaseError.toException())
                }
            }
            FBRef.boardRef.child(commentBoardKeyList[position]).addValueEventListener(postListener)

            itemView.setOnClickListener {
                // 내부페이지로 이동
                val intent = Intent(context, BoardInsideActivity::class.java)
                intent.putExtra("key", commentBoardKeyList[position])
                itemView.context.startActivity(intent)
            }
        }
    }
}