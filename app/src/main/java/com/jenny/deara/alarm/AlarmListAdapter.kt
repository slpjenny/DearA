package com.jenny.deara.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R

class AlarmListAdapter(val context: Context, val alarmkeyList: MutableList<String>, val OnClickInterface: OnClickInterface): RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

    var datas = mutableListOf<AlarmData>()

    interface SwitchClickListener{
        fun onClicked(position: Int, OnOff: Boolean)
    }

    interface DataClickListener {
        fun onClick(view: View, position: Int)
    }

    //전달된 객체를 저장할 변수 정의
    private lateinit var dataClickListener: DataClickListener
    private lateinit var onClickedListener: SwitchClickListener

    fun setOnClickedListener(listener: SwitchClickListener){
        onClickedListener = listener
    }

    fun setDataClickListener(dataClickListener: DataClickListener) {
        this.dataClickListener = dataClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alarm_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: AlarmListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position], alarmkeyList[position])

        holder.itemView.setOnClickListener {
            dataClickListener.onClick(it, position)
        }

        val OnOffBtn: Switch = holder.itemView.findViewById(R.id.OnOffBtn)

        OnOffBtn.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                onClickedListener.onClicked(position,true)
            } else {
                onClickedListener.onClicked(position,false)
            }
        }


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val time: TextView = itemView.findViewById(R.id.tv_time)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val day: TextView = itemView.findViewById(R.id.tv_day)
        private val OnOffBtn: Switch = itemView.findViewById(R.id.OnOffBtn)

        fun bind(item: AlarmData, key: String) {
            time.text = item.time
            title.text = item.title
            day.text = item.day
            OnOffBtn.isChecked = item.onOff
            }

    }

}
