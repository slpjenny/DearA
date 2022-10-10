package com.jenny.deara.alarm

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jenny.deara.R
import com.jenny.deara.TodoDialog
import com.jenny.deara.mypages.ToDoModel
import com.jenny.deara.mypages.TodoAdapter

class AlarmListAdapter(val context: Context): RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

    var datas = mutableListOf<AlarmData>()

    interface DataClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var dataClickListener: DataClickListener

    fun setDataClickListener(dataClickListener: DataClickListener) {
        this.dataClickListener = dataClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alarm_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: AlarmListAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])

        holder.itemView.setOnClickListener {
            dataClickListener.onClick(it, position)
        }
    }

    /*   fun removeItem(position: Int) {
               datas.removeAt(position)
               notifyDataSetChanged()
       }*/

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val time: TextView = itemView.findViewById(R.id.tv_time)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val day: TextView = itemView.findViewById(R.id.tv_day)

        fun bind(item: AlarmData) {
            time.text = item.time
            title.text = item.title
            day.text = item.day

            }

    }
}
