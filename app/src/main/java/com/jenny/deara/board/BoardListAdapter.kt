package com.jenny.deara.board

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.board.comment.CommentModel
import com.jenny.deara.utils.FBAuth


class BoardListAdapter(val context: Context,
                       var boardkeyList : MutableList<String>,
                       var menu: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas = mutableListOf<BoardModel>()
    var myComments = mutableListOf<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        if (menu == "myComment"){ // 댓글로 연결
            view = LayoutInflater.from(context).inflate(R.layout.comment_my_list_item, parent,false)
            return CommentViewHolder(view)
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.board_list_item, parent,false)
            return ViewHolder(view)
        }
    }

    // 데이터의 사이즈는 board list의 사이즈
    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.bind(datas[position], boardkeyList[position])

        if(menu == "myComment"){
            val obj = myComments[position]
            (holder as CommentViewHolder).bind(obj)
        }else{
            val obj = datas[position]
            val obj2 = boardkeyList[position]
            (holder as ViewHolder).bind(obj, obj2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: MutableList<BoardModel>, keyList: MutableList<String>) {
        datas = list
        boardkeyList = keyList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.boardTitle)
        private val time: TextView = itemView.findViewById(R.id.boardTime)
        private val sort: TextView = itemView.findViewById(R.id.boardSort)
        private val uid: TextView = itemView.findViewById(R.id.boardWriter)

        fun bind(item: BoardModel, key: String) {
            title.text = item.title
            time.text = item.time
            sort.text = item.sort
            //uid.text = item.uid
            uid.text = FBAuth.getNick(item.uid)

            // 내부페이지로 이동
            itemView.setOnClickListener {
                val intent = Intent(context, BoardInsideActivity::class.java)
                intent.putExtra("key", boardkeyList[position])
                itemView.context.startActivity(intent)
            }
        }

    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val comment: TextView = itemView.findViewById(R.id.contentArea)
        private val time: TextView = itemView.findViewById(R.id.timeArea)

        fun bind(item: CommentModel) {
            comment.text = item.content
            time.text = item.time
            // 글제목 추가하기

            // 내부페이지로 이동
            itemView.setOnClickListener {
//                val intent = Intent(context, BoardInsideActivity::class.java)
//                intent.putExtra("key", boardkeyList[position])
//                itemView.context.startActivity(intent)
                Log.d("댓글 클릭 이벤트", "Hi, CommentViewHolder")
            }
        }
    }
}