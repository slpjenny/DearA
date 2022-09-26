package com.jenny.deara.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class DiaryListAdapter(val context: Context): RecyclerView.Adapter<DiaryListAdapter.ViewHolder>(){

    var datas = mutableListOf<DiaryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  DiaryListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.diary_list_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder:  DiaryListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title1: TextView = itemView.findViewById(R.id.title1Area)
        private val contents1: TextView = itemView.findViewById(R.id.content1Area)
        private val title2: TextView = itemView.findViewById(R.id.title2Area)
        private val contents2: TextView = itemView.findViewById(R.id.content2Area)

        fun bind(item: DiaryData) {
            title1.text = item.title1
            contents1.text = item.contents1
            title2.text = item.title2
            contents2.text = item.contents2
        }
    }
}