package com.jenny.deara.board

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jenny.deara.R
import com.jenny.deara.board.comment.CommentListAdapter
import kotlinx.android.synthetic.main.image_list_item.view.*
import java.security.KeyStore

class ImageListAdapter(val context: Context)
    : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    var datas = mutableListOf<Uri>()

    // 커스텀 리스너
    interface OnDelClickListener{
        fun onDelClick(v: View, position : Int)
    }

    // 리스너 객체 참조를 저장하는 변수
    private lateinit var mOnDelClickListener : OnDelClickListener

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnDelClickListener(onDelClickListener: OnDelClickListener){
        mOnDelClickListener = onDelClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ImageListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  ImageListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var image: ImageView = itemView.findViewById(R.id.image)
        val delBtn: ImageView = itemView.findViewById(R.id.delBtn)

        init{
            delBtn.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && mOnDelClickListener != null){
                    mOnDelClickListener.onDelClick(view, pos)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: Uri) {
            Glide.with(itemView)
                .load(item)
                .into(itemView.image)
            image.clipToOutline = true

//            delBtn.setOnClickListener {
//                datas.removeAt(position)
//                notifyDataSetChanged()
//            }
        }
    }
}