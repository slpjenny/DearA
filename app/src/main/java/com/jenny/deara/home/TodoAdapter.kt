package com.jenny.deara.home

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jenny.deara.R
import com.jenny.deara.alarm.AlarmData
import com.jenny.deara.databinding.TodolistviewItemBinding
import com.jenny.deara.utils.FBAuth
import com.jenny.deara.utils.FBRef
import java.lang.reflect.Array.get

val TAG = TodoAdapter::class.java.simpleName

class TodoAdapter(val context : Context, val items: ArrayList<ToDoData>,val todokeyList : MutableList<String>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.todolistview_item, parent,false)
        return TodoViewholder(view)
    }

    override fun onBindViewHolder(holder: TodoViewholder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int = items.size

   inner class TodoViewholder(view : View) : RecyclerView.ViewHolder(view){

       private val todowrite : TextView = itemView.findViewById(R.id.todowrite)
       private var clearcheck : CheckBox = itemView.findViewById(R.id.clear_check)
       private var tododel : ImageButton = itemView.findViewById(R.id.tododel)

        fun bind(todo : ToDoData) {

            todowrite.text = todo.todo
            clearcheck.isChecked = todo.check


            // 할 일 목록 삭제
            tododel.setOnClickListener {

                val key = items[adapterPosition].key
                FBRef.todoRef.child(key).removeValue()
                items.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyDataSetChanged()

            }

            // 할 일 목록 체크시
            clearcheck.setOnClickListener {

                // 완료한 항목 체크할 때
                if (items[adapterPosition].check == false){

                    items[adapterPosition].check = true
                    FBRef.todoRef
                        .child(items[adapterPosition].key)
                        .child("check")
                        .setValue(items[adapterPosition].check)

                } else {    // 완료되지 않은 항목 체크할 때
                    items[adapterPosition].check = false

                    FBRef.todoRef
                        .child(items[adapterPosition].key)
                        .child("check")
                        .setValue(items[adapterPosition].check)
                }

            }
        }
   }
    

    }