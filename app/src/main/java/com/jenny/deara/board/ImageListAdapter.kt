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
import kotlinx.android.synthetic.main.image_list_item.view.*
import java.security.KeyStore

class ImageListAdapter(val context: Context)
    : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    var datas = mutableListOf<Uri>()

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

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: Uri) {
            Glide.with(itemView)
                .load(item)
                .into(itemView.image)
            image.clipToOutline = true

            delBtn.setOnClickListener {
                datas.removeAt(position)
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }
        }
    }
}