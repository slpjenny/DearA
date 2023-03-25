package com.jenny.deara.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.utils.FBRef

class PillListAdapter(val context: Context, var pillList: MutableList<pillData>):RecyclerView.Adapter<PillListAdapter.ViewHolder>(){

    var pills = mutableListOf<pillData>()

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

            // 아이템 삭제
            val removeImv = itemView.findViewById<ImageView>(R.id.removeImv)

            removeImv.setOnClickListener {

                // 리싸이클러뷰에서 아이템 삭제로 할 수 있긴한데,
                // 파이어베이스에서 내용 자체를 삭제하고 notifyDataChanged() 하면
                // 한번에 저장된거랑, 아이템 자체 삭제 둘 다 될 수 있음
                // 근데 FB에 저장할 모델을 못정했음

//                private fun removeRecord(key : String){
//                    FBRef.recordRef.child(key).removeValue()
//                    Toast.makeText(baseContext,"삭제가 완료되었습니다.", Toast.LENGTH_LONG).show()
//
//                    finish()
//                }

                // RecyclerView 레이아웃에서만 먼저 삭제
                pillList.removeAt(adapterPosition)
                notifyDataSetChanged()

                // 파이어베이스에서도 삭제하기
//                FBRef.pillRef.child(key).removeValue()





            }
        }

    }


}