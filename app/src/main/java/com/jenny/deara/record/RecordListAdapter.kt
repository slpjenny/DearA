package com.jenny.deara.record

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class RecordListAdapter(val context: Context, val recordList : MutableList<String>): RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {

    var datas = mutableListOf<RecordData>()


    // 뷰 홀더가 처음 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recordlist_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    // 재활용 하는 곳
    // 각각의 자리에 알맞은 값을 넣어준다.
    override fun onBindViewHolder(holder: RecordListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position],recordList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.recordTitle)
        private val date: TextView = itemView.findViewById(R.id.recordDate)
        private val time: TextView = itemView.findViewById(R.id.recordTime)


        // RecordData 객체에서 이것만 리싸이클러뷰 데이터로 만들거임
        fun bind(item: RecordData, s: String) {
            title.text = item.hospitalName
            date.text = item.date
            time.text = item.time

            // 화살표 버튼 클릭시 상세 페이지
            val detailBtn = itemView.findViewById<ImageView>(R.id.detailBtn)

            detailBtn.setOnClickListener {
                val intent = Intent(context, EditRecordActivity::class.java)
                // 해당 Key로 몇번째 아이템인지 구별
                intent.putExtra("key", recordList[position])

//                Log.d("itsthekey",recordList[position])
                // -NRMoW5-QaoZI0ph6gRe   ..? 왜..?

                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener {
                val detailIntent = Intent(context, EditRecordActivity::class.java)
                detailIntent.putExtra("key", recordList[position])

                itemView.context.startActivity(detailIntent)
            }

        }


    }

}