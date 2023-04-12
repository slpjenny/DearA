package com.jenny.deara.record

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.utils.FBRef

class PillListAdapter(val context: Context, val pillList: MutableList<String>):RecyclerView.Adapter<PillListAdapter.ViewHolder>(){

    var pills = mutableListOf<pillData>()

    // 뷰 홀더가 처음 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pilllist_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pills.size
    }

    fun updateReceiptsList(newList: MutableList<pillData>) {
        pills.clear()
        pills.addAll(newList)
        this.notifyDataSetChanged()
    }

    // 추가한 부분
    public fun addItem(data: pillData){
        pills.add(data)
        notifyItemInserted(pills.size-1)
    }

    // 재활용 하는 곳
    // 각각의 자리에 알맞은 값을 넣어준다.
    override fun onBindViewHolder(holder: PillListAdapter.ViewHolder, position: Int) {
        holder.bind(pills[position], pillList[position])

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val pillName: TextView = itemView.findViewById(R.id.pillName)
        private val dosage: TextView = itemView.findViewById(R.id.dosage)


        // RecordData 객체에서 이것만 리싸이클러뷰 데이터로 만들거임
        fun bind(item: pillData, s: String) {
            pillName.text = item.pillName
            dosage.text = item.dosage

            // 아이템 삭제
            val removeImv = itemView.findViewById<ImageView>(R.id.removeImv)

            removeImv.setOnClickListener {

                val pillKey = pillList[position]

                Log.d ("key", pillKey)
                // pillData(pillName=11, dosage=11, uid=OFPEYLKeieN02lJHgxPzX3XRWvc2, itsRecordkey=-NRMbrHycMfg66bNokn5)

                // RecyclerView 레이아웃에서만 먼저 삭제
//                pillList.removeAt(adapterPosition)
//                notifyDataSetChanged()

                FBRef.pillRef.child(pillKey).removeValue()
                notifyItemRemoved(adapterPosition)


            }
        }

    }


}