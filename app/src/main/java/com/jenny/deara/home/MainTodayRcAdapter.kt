package com.jenny.deara.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.MainActivity
import com.jenny.deara.R
import com.jenny.deara.record.*
import org.w3c.dom.Text

class MainTodayRcAdapter(val context: Context, val mainRecordList : MutableList<String>): RecyclerView.Adapter<MainTodayRcAdapter.ViewHolder>()  {

    var datas = mutableListOf<MainTodayRecordData>()


    // 뷰 홀더가 처음 생성될 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTodayRcAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_today_record_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    // 재활용 하는 곳
    // 각각의 자리에 알맞은 값을 넣어준다.
    override fun onBindViewHolder(holder: MainTodayRcAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position],mainRecordList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val hospitalName : TextView = itemView.findViewById(R.id.hospitalName)
        private val hospitalTime : TextView = itemView.findViewById(R.id.hospitalTime)


        // RecordData 객체에서 이것만 리싸이클러뷰 데이터로 만들거임
        fun bind(item: MainTodayRecordData, s: String) {
            hospitalName.text = item.hospitalName
            hospitalTime.text = item.date

            // 아이템 클릭시, 진료기록 fragment로 이동
            itemView.setOnClickListener {

                val intent = Intent(context, EditRecordActivity::class.java)
                intent.putExtra("todayKey", mainRecordList[position])

                itemView.context.startActivity(intent)
            }
        }


    }
}