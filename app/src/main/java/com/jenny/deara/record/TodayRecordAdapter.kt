package com.jenny.deara.record

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class TodayRecordAdapter(val context: Context, val todayRecordList:MutableList<String>):
    RecyclerView.Adapter<TodayRecordAdapter.ViewHolder>() {

    var datas = mutableListOf<TodayRecordData>()


    // 뷰 홀더가 처음 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayRecordAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.today_record_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    // 재활용 하는 곳
    // 각각의 자리에 알맞은 값을 넣어준다.
    override fun onBindViewHolder(holder: TodayRecordAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position],todayRecordList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.tdRecordTitle)
        private val date: TextView = itemView.findViewById(R.id.tdRecordDate)
        private val time: TextView = itemView.findViewById(R.id.tdRecordTime)


        // RecordData 객체에서 이것만 리싸이클러뷰 데이터로 만들거임
        fun bind(item: TodayRecordData, s: String) {
            title.text = item.hospitalName
            date.text = item.date
            time.text = item.time

            // 화살표 버튼 클릭시 상세 페이지
            val tdDetailBtn = itemView.findViewById<ImageView>(R.id.tdDetailBtn)

            tdDetailBtn.setOnClickListener {
                val intent = Intent(context, EditRecordActivity::class.java)
                // 해당 Key로 몇번째 아이템인지 구별
                intent.putExtra("key", todayRecordList[position])

                itemView.context.startActivity(intent)
            }

        }


    }

}