package com.jenny.deara.record

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class PillListAdapter (val context: Context, val pillList:MutableList<pillData>):RecyclerView.Adapter<PillListAdapter.ViewHolder>(){

//    var pills = mutableListOf<pillData>()

    // 뷰 홀더가 처음 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pilllist_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pillList.size
    }

    // 재활용 하는 곳
    // 각각의 자리에 알맞은 값을 넣어준다.
    override fun onBindViewHolder(holder: PillListAdapter.ViewHolder, position: Int) {
        holder.bind(pillList[position])
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val pillName: TextView = itemView.findViewById(R.id.pillName)
        private val dosage: TextView = itemView.findViewById(R.id.dosage)


        // RecordData 객체에서 이것만 리싸이클러뷰 데이터로 만들거임
        fun bind(item: pillData) {
            pillName.text = item.pillName
            dosage.text = item.dosage

        }


    }


}